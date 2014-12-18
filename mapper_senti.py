#!/usr/bin/env python
"""A more advanced Mapper, using Python iterators and generators."""

import sys

def read_input(file):
    for line in file:
        # split the line into words
        yield line.split()

def main(separator='\t'):
    # input comes from STDIN (standard input)
    data = read_input(sys.stdin)

    ifile = open('twitter_sentiment_list.csv', 'r')
    happy_log_probs = {}
    sad_log_probs = {}
    ifile.readline() #Ignore title row
    
    for line in ifile:
        tokens = line[:-1].split(',')
        happy_log_probs[tokens[0]] = float(tokens[1])
        sad_log_probs[tokens[0]] = float(tokens[2])
    
    for i, row in enumerate(data):
        # write the results to STDOUT (standard output);
        # what we output here will be the input for the
        # Reduce step, i.e. the input for reducer.py
        #
        # tab-delimited; the trivial word count is 1
        #for word in row:
         #   print '%s%s%d' % (word, separator, 1)

        happy_probs = [happy_log_probs[word] for word in row if word in happy_log_probs]
        sad_probs = [sad_log_probs[word] for word in row if word in sad_log_probs]

        # Sum all the log-probabilities for each sentiment to get a log-probability for the whole tweet
        tweet_happy_log_prob = np.sum(happy_probs)
        tweet_sad_log_prob = np.sum(sad_probs)

        # Calculate the probability of the tweet belonging to each sentiment
        prob_happy = np.reciprocal(np.exp(tweet_sad_log_prob - tweet_happy_log_prob) + 1)
        prob_sad = 1 - prob_happy
        print '%d%s%f' % (i, separator, prob_happy) 

if __name__ == "__main__":
    main()