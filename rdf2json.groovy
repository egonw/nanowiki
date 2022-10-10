// @Grab(group='org.slf4j', module='jcl-over-slf4j', version='2.0.3')

@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.0.45')
@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.0.45')

workspaceRoot = "."
ui = new net.bioclipse.managers.UIManager(workspaceRoot);
bioclipse = new net.bioclipse.managers.BioclipseManager(workspaceRoot);
rdf = new net.bioclipse.managers.RDFManager(workspaceRoot);

store = rdf.createInMemoryStore()
rdf.importFile(store, "/backup.ttl.txt", "Turtle")

sparql = """
PREFIX wiki: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?substance ?label ?identifier WHERE {
  ?substance a wiki:Category-3AMaterials ;
    wiki:Property-3AHas_Identifier ?identifier ;
    rdfs:label ?label.
}
"""

results = rdf.sparql(store, sparql)
// println "results: " + results

for (i in 0..(results.rowCount-1)) {
  substance = results.get(i, "substance").replace("wiki:", "http://127.0.0.1/mediawiki/index.php/Special:URIResolver/")
  identifier = results.get(i, "identifier")
  label = results.get(i, "label")
  nmFile = "/docs/nanowiki${identifier}.md"
  // println "NM file: $nmFile"
  ui.renewFile(nmFile)
  ui.append(nmFile, "# ${label}\n")
  ui.append(nmFile, "<a name=\"material\" />\n")
  ui.append(nmFile, "<script type=\"application/ld+json\">\n")
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
    "sameAs: "${substance}"
  }
""")
  ui.append(nmFile, "</script>\n\n")
}

