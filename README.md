assign3
=======

UNIs:
al3372
bs2888,
cd2789,
kmw2168

Approach:

We first tried the positive and negative word lists but that didn't give satisfactory answers. So,
we used nltk (by bootstrapping it to the master node) to calculate the likelihood of a word (or emotiocon
or any token) being positive or negative. These results are stored in sentiments_list.csv

For word trends, run EMR in streaming mode with mapper.py and reducer.py as the mapper and reducer.
for querying the sentiment toward one topic, run sentiment.py as mapper and 'aggregate' as reducer.

For getting sentiments of tweets as a unit, use mapper_senti.py as the mapper and 'aggregate' as reducer.

Running on the 4.3G dataset produced huge output sets in some cases, so we've included the screenshot for that and
put some outputs in Outputs folder.
