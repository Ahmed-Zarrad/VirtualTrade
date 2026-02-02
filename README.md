# VirtualTrade
A high-performance stock trading simulation and investment analysis platform built for academic research using Spring Boot, MongoDB, and Redis.
📖 Overview

VirtualTrade is an academic project designed to simulate a real-world stock market environment. It provides a risk-free platform for users to practice trading strategies, manage virtual portfolios, and analyze market trends.

The primary goal of this project is to demonstrate the handling of "Big Data" characteristics—specifically high Velocity (ingesting real-time price ticks) and high Volume (storing vast amounts of historical financial data) in a distributed system environment. It leverages a polyglot persistence architecture to optimize for both real-time responsiveness and complex analytical queries.

This project was developed for the Large Scale and Multi Structured Databases course at the University of Pisa.
✨ Key Features

    📈 Real-Time Market Monitoring: View up-to-the-second stock prices and market movements.

    💰 Paper Trading Simulator: Execute buy and sell orders using virtual currency in a realistic environment.

    💼 Portfolio Management: Track holdings, analyze performance (P&L), and view historical transactions.

    📊 Advanced Analytics: Utilize aggregation pipelines to analyze sector performance, volatility, and historical trends.

    📰 Market News: Integrated news feed to provide context for price movements.

🏗️ Technical Architecture

This project uses a microservice-based architecture powered by Spring Boot and utilizes specific NoSQL databases for targeted use cases.
Tech Stack

    Backend Framework: Java / Spring Boot

    Containerization: Docker & Docker Compose


Database Design (Polyglot Persistence)

The core of VirtualTrade lies in its specialized data layer:
1. Redis (High Velocity Layer)

Used as a high-speed caching and real-time data layer.

    Real-time Price Cache: Stores the absolute latest price of stocks for immediate retrieval by the UI, bypassing slower disk-based reads.

    Session Management: Handles user authentication sessions.

    Leaderboards: Utilizes Sorted Sets for real-time "Top Gainers/Losers" rankings.

2. MongoDB (High Volume & Analytics Layer)

Used as the persistent data store for complex data structures and historical analysis.

    Historical Market Data: Stores massive amounts of historical price ticks using the Bucketing Pattern (one document per hour containing an array of minute-ticks) to optimize storage and read performance for time-series charts.

    Portfolios & Users: Stores user profiles and current holdings using the Subset Pattern for efficient retrieval.

    Complex Aggregations: Utilizes MongoDB's powerful aggregation framework for deep analytical queries across large datasets.



⚠️ Disclaimer

VirtualTrade is for educational and informational purposes only. All trading is executed with virtual currency. No real money is involved, and the data provided should not be used for actual financial investment decisions.
