library(rrdf)

endpoint = "http://127.0.0.1/mediawiki/index.php/Special:SPARQLEndpoint"

query = "
PREFIX w: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>

SELECT ?size ?min ?max
WHERE {
  ?s a <http://semantic-mediawiki.org/swivt/1.0#Subject> .
  ?s w:Property-3AHas_Size_Units ?units .
  OPTIONAL { ?s w:Property-3AHas_Size ?size }
  OPTIONAL { ?s w:Property-3AHas_Size_Min ?min }
  OPTIONAL { ?s w:Property-3AHas_Size_Max ?max }
}
"

data = sparql.remote(endpoint, query)
sizes = as.numeric(data[,"size"])

missing = which(is.na(data[,"size"]))
sizes[missing] = apply(data[missing,c("min","max")], MARGIN=1, function(x) ( mean(as.numeric(x)) ))

pdf(file="sizesHistogram.pdf")
hist(sizes[-c(20,22)], breaks=40)
dev.off()
