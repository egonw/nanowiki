@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.1.2')
@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.1.2')

workspaceRoot = "."
ui = new net.bioclipse.managers.UIManager(workspaceRoot);
bioclipse = new net.bioclipse.managers.BioclipseManager(workspaceRoot);
rdf = new net.bioclipse.managers.RDFManager(workspaceRoot);

store = rdf.createInMemoryStore()
rdf.importFile(store, "/backup.ttl.txt", "Turtle")

// Materials

sparql = """
PREFIX wiki: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?substance ?label ?identifier ?paper ?paperLabel WHERE {
  ?substance a wiki:Category-3AMaterials ;
    wiki:Property-3AHas_Identifier ?identifier ;
    rdfs:label ?label.
  OPTIONAL { ?substance wiki:Property-3AHas_Source ?paper . ?paper rdfs:label ?paperLabel}
}
"""

results = rdf.sparql(store, sparql)
// println "results: " + results

for (i in 1..results.rowCount) {
  substance = results.get(i, "substance").replace("wiki:", "http://127.0.0.1/mediawiki/index.php/Special:URIResolver/")
  identifier = results.get(i, "identifier")
  label = results.get(i, "label")
  nmFile = "/docs/nanowiki${identifier}.md"
  paper = results.get(i, "paper")
  paperLabel = ""
  if (paper) {
    paper = paper.replace("wiki:", "http://127.0.0.1/mediawiki/index.php/Special:URIResolver/")
    paperLabel = results.get(i, "paperLabel")
  }
  // println "NM file: $nmFile"
  ui.renewFile(nmFile)
  ui.append(nmFile, "<a name=\"material\" />\n\n")
  ui.append(nmFile, "# ${label}\n")
  ui.append(nmFile, "<script type=\"application/ld+json\">")
  ui.append(nmFile,"""
  {
    "@context": "https://schema.org/",
    "@type": "ChemicalSubstance",
    "http://purl.org/dc/terms/conformsTo":
      {
        "@type": "CreativeWork",
        "@id": "https://bioschemas.org/profiles/ChemicalSubstance/0.4-RELEASE/"
      },
    "@id": "https://egonw.github.io/nanowiki/nanowiki${identifier}.html#material",
    "name": "${label}",
    "sameAs": "${substance}"
  }
""")
  ui.append(nmFile, "</script>\n\n")
  ui.append(nmFile, "\n")
  if (paper) ui.append(nmFile, "* Source: [${paperLabel}](${paper})\n")
}

// Articles

sparql = """
PREFIX wiki: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
prefix owl:  <http://www.w3.org/2002/07/owl#>

SELECT DISTINCT ?paper ?label ?identifier ?doiURL ?journal ?year WHERE {
  ?paper a wiki:Category-3APapers ;
    wiki:Property-3AHas_DOI ?identifier ;
    rdfs:label ?label ;
    owl:sameAs ?doiURL .
  OPTIONAL { ?paper wiki:Property-3AHas_Journal / rdfs:label ?journal }
  OPTIONAL { ?paper wiki:Property-3AHas_Year ?year }
}
"""

results = rdf.sparql(store, sparql)
// println "results: " + results

for (i in 1..results.rowCount) {
  paper = results.get(i, "paper")
  // println "Paper: ${paper}\n"
  identifier = results.get(i, "identifier")
  doiURL = results.get(i, "doiURL")
  label = results.get(i, "label")
  year = results.get(i, "year")
  journal = results.get(i, "journal")
  artFile = "/docs/article${label}.md"
  // println "Article file: artFile"
  ui.renewFile(artFile)
  ui.append(artFile, "<a name=\"article\" />\n\n")
  ui.append(artFile, "# ${label}\n\n")
  ui.append(artFile, "* Year: ${year}\n")
  ui.append(artFile, "* Journal: ${journal}\n")
  ui.append(artFile, "* DOI: <a href=\"${doiURL}\">${identifier}</a>\n")

  // list the materials from this article
  ui.append(artFile, "\n## Materials\n")
  sparql = """
PREFIX wiki: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?substance ?identifier ?label WHERE {
  ?substance wiki:Property-3AHas_Source ${paper} ;
    wiki:Property-3AHas_Identifier ?identifier ;
    rdfs:label ?label.
}
"""
  materials = rdf.sparql(store, sparql)
  if (materials.rowCount > 0) {
    for (j in 1..materials.rowCount) {
      substance = materials.get(j, "substance").replace("wiki:", "http://127.0.0.1/mediawiki/index.php/Special:URIResolver/")
      identifier = materials.get(j, "identifier")
      label = materials.get(j, "label")
      nmFile = "nanowiki${identifier}.md"
      ui.append(artFile, "* [${label}](${nmFile})\n")
    }
  }
  
}

