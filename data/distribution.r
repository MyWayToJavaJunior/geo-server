library('plyr');
library('data.table');
library('ggplot2');

features <- read.table("distribution", header=FALSE, sep=",")
features[is.na(features)] <- 0
features <- rename(features, c('V1' = 'geo_hash', 'V2' = 'size'))
featuresTable <- data.table(features)

one <- featuresTable[featuresTable$size > 1500]
two <- featuresTable[featuresTable$size < 1500]
#ggplot(featuresTable, aes(x=featuresTable$geo_hash, y=featuresTable$size)) + geom_point(shape=1) + geom_smooth(method=lm)


