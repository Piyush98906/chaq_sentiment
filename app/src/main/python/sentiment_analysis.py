import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import praw
import nltk
# Create a SentimentIntensityAnalyzer object
from nltk.sentiment.vader import SentimentIntensityAnalyzer as SIA

import nltk

nltk.download('vader_lexicon')

reddit = praw.Reddit(
    client_id="ZwlKIamQj_Wbm2gY_9Rt-g",
    client_secret="UdzRKhd0IcMe9J2W33lnqO4QgnUbGg",
    user_agent="scrapper 1.0",
)


def perform_sentiment_analysis(word, limit=1000):
    headlines = set()
    sia = SIA()
    results = []

    # Search Reddit for the given word
    submissions = reddit.subreddit('all').search(word, limit=limit)
    for submission in submissions:
        headlines.add(submission.title)

    # Perform sentiment analysis on the headlines
    for line in headlines:
        pol_score = sia.polarity_scores(line)
        pol_score['headline'] = line
        results.append(pol_score)

    # Create DataFrame from sentiment analysis results
    df = pd.DataFrame.from_records(results)

    # Assign labels based on compound score
    df.loc[df['compound'] > 0.2, 'label'] = 1  # Positive sentiment
    df.loc[df['compound'] < -0.5, 'label'] = -1  # Negative sentiment
    df.loc[(df['compound'] >= -0.2) & (df['compound'] <= 0.2), 'label'] = 0  # Neutral sentiment

    # Generate plot
    plt.figure(figsize=(8, 6))
    counts = df.label.value_counts(normalize=True) * 100
    sns.barplot(x=counts.index, y=counts)
    sns.barplot(x=counts.index, y=counts, hue=counts.index,
                palette=['red', 'yellow', 'green'])  # Specify colors for each bar
    plt.xlabel('Sentiment')
    plt.ylabel('Percentage')
    plt.xticks([0, 1, 2], ['Negative', 'Neutral', 'Positive'])
    plt.title('Sentiment Analysis Results')
    plt.tight_layout()

    # Save plot to a file
    plot_path = '/data/user/0/com.example.chaq_sentiment/files/plot.png'
    plt.savefig(plot_path)

    # Extract top 5 headlines for  sentiment
    positive = df[df['label'] == 1].nlargest(2, 'compound')['headline']
    neutral = df[df['label'] == 0].nlargest(2, 'compound')['headline']
    negative = df[df['label'] == -1].nsmallest(2, 'compound')['headline']
    positive_headlines = positive.tolist()
    negative_headlines = negative.tolist()
    neutral_headlines = neutral.tolist()

    return [positive_headlines, neutral_headlines, negative_headlines]
