from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer


def analyze_sentiment(text):
    # Initialize VADER sentiment analyzer
    analyzer = SentimentIntensityAnalyzer()

    # Analyze sentiment
    sentiment_score = analyzer.polarity_scores(text)

    # Convert scores to percentages
    positive_percent = sentiment_score['pos'] * 100
    neutral_percent = sentiment_score['neu'] * 100
    negative_percent = sentiment_score['neg'] * 100
    return [positive_percent, neutral_percent, negative_percent]