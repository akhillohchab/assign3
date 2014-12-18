assign3
=======

UNIs:
al3372
bs2888,
cd2789,
kmw2168



Approach:

We first tried the positive and negative word lists but that didn't give satisfactory answers. So,
we used nltk (by bootstrapping it to the master node) to classify a word (or emotiocon
or any token) being positive or negative. The sentiments for each word are stored in sentiments_list.csv with the log of probability of each word
being happy or sad.

For word trends, run EMR in streaming mode with mapper.py and reducer.py as the mapper and reducer.
for querying the sentiment toward one topic, run sentiment.py as mapper and 'aggregate' as reducer.

For getting sentiments of tweets as a unit, use sent_list.py as the mapper and 'aggregate' as reducer. Note that you will also have to add the 
sentiments_list.csv file to the S3 bucket to run this. 
A sample of the type of output you get from this can be found in /Outputs/'tweets**_output.ext' where the probabilities of the tweet sentiment 
range from [0,1] with 1 being happy and 0 being sad.

Running on the 4.3G dataset produced huge output sets in some cases, so we've included the screenshot for that and
put some outputs in Outputs folder.


Files:

This assignment uses some files from our submission for assignment 2 (particularly tweetget.java) which we used
to generate more tweets to test our code out. These files are 'tweets.*.txt'.
