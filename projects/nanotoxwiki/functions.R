
puzynDescriptor = function(composition) {
  val = NA;
  if (length(grep("Al", composition)) > 0) {
    val = 1187.83;
  } else if (length(grep("Bi", composition)) > 0) {
    val = 1137.40; 
  } else if (length(grep("Co", composition)) > 0) {
    val = 601.80; 
  } else if (length(grep("Cr", composition)) > 0) {
    val = 1268.70; 
  } else if (length(grep("Cu", composition)) > 0) {
    val = 706.25; 
  } else if (length(grep("Fe", composition)) > 0) {
    val = 1408.29; 
  } else if (length(grep("In", composition)) > 0) {
    val = 1271.13; 
  } else if (length(grep("La", composition)) > 0) {
    val = 1017.22; 
  } else if (length(grep("Ni", composition)) > 0) {
    val = 576.90; 
  } else if (length(grep("Sb", composition)) > 0) {
    val = 1233.06; 
  } else if (length(grep("Si", composition)) > 0) {
    val = 1686.38; 
  } else if (length(grep("Sn", composition)) > 0) {
    val = 1717.32; 
  } else if (length(grep("Ti", composition)) > 0) {
    val = 1575.73; 
  } else if (length(grep("V", composition)) > 0) {
    val = 1079.73; 
  } else if (length(grep("Y", composition)) > 0) {
    val = 837.15; 
  } else if (length(grep("Zn", composition)) > 0) {
    val = 662.44;
  } else if (length(grep("Zr", composition)) > 0) {
    val = 1357.66; 
  }
  val;
}

