library(rrdf)

endpoint = "http://127.0.0.1/mediawiki/index.php/Special:SPARQLEndpoint"

query = "
PREFIX w: <http://127.0.0.1/mediawiki/index.php/Special:URIResolver/>

SELECT ?zeta
WHERE { [] w:Property-3AHas_Zeta_potential ?zeta }
"

data = sparql.remote(endpoint, query)
zetas = data[,"zeta"]

hist(zetas, breaks=40)
