library(rrdf)

endpoint = "http://127.0.0.1/mediawiki/index.php/Special:SPARQLEndpoint"

query = "
PREFIX w: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>
PREFIX cat: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/Category-3A>
PREFIX prop: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/Property-3A>

SELECT ?nm ?comp ?dose ?value
WHERE {
  ?study ?p w:Karlsson2008 .
  ?nm ?q ?study ;
      prop:Has_Chemical_Composition ?comp ;
      a cat:NanoMaterials .
  ?m prop:Has_Entity ?nm ;
     prop:Has_Dose ?dose ;
     prop:Has_Endpoint_Value ?value .
}
"

data = sparql.remote(endpoint, query)
sizes = as.numeric(data[,"size"])

missing = which(is.na(data[,"size"]))
sizes[missing] = apply(data[missing,c("min","max")], MARGIN=1, function(x) ( mean(as.numeric(x)) ))

pdf(file="sizesHistogram.pdf")
hist(
  sizes[-c(20,22)], breaks=40,
  main="Distribution of NM sizes"
)
dev.off()
