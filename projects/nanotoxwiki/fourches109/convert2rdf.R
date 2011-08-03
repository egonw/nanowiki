library(rrdf)

store = new.rdf()

data = read.csv("fourches109.csv", header=FALSE)

apply(data, MARGIN=1, function(x) {
  add.data.triple(store,
    paste("<http://", ">", sep=""),
    "",
    data[3]
  );
})

