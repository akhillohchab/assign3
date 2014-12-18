import numpy as np

def readSentimentList(file_name):
    ifile = open(file_name, 'r')
    happy_log_probs = {}
    sad_log_probs = {}
    ifile.readline() #Ignore title row
    
    for line in ifile:
        tokens = line[:-1].split(',')
        happy_log_probs[tokens[0]] = float(tokens[1])
        sad_log_probs[tokens[0]] = float(tokens[2])

    return happy_log_probs, sad_log_probs

def classifySentiment(words, happy_log_probs, sad_log_probs):
    # Get the log-probability of each word under each sentiment
    line = words.split()
   
    happy_probs = [happy_log_probs[word] for word in line if word in happy_log_probs ]
    sad_probs = [sad_log_probs[word] for word in line if word in sad_log_probs ]
    
        
    # Sum all the log-probabilities for each sentiment to get a log-probability for the whole tweet
    tweet_happy_log_prob = np.sum(happy_probs)
    tweet_sad_log_prob = np.sum(sad_probs)

    print (tweet_happy_log_prob)
    # Calculate the probability of the tweet belonging to each sentiment
    prob_happy = np.reciprocal(np.exp(tweet_sad_log_prob - tweet_happy_log_prob) + 1)
    prob_sad = 1 - prob_happy

    return prob_happy, prob_sad

def main():
    # We load in the list of words and their log probabilities
    happy_log_probs, sad_log_probs = readSentimentList('sentiment_list.csv')
    file = 'tweets.Dec17-2356.txt'
    file2 = file+'_output.txt'
    f2 = open(file2, 'w')
    with open(file) as f:
        lines = f.readlines()
        # split the line into words
        #yield line.split()
        for line in lines:
            full = line.split(',')
                  # Calculate the probabilities that the tweets are happy or sad
            tweet = full[1].split(':')
            filtered = tweet[1].strip()
            tweet_happy_prob, tweet_sad_prob = classifySentiment(filtered, happy_log_probs, sad_log_probs)
            
            f2.write(repr(filtered)+': '+repr(tweet_happy_prob)+'\t')
            #print ("The probability for tweet is ", tweet_happy_prob)
        
if __name__ == '__main__':
    main()
