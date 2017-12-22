## Java_API

### Requirements:
You will need the latest Netbeans IDE or Eclipse IDE and Java runtime Environment, you can download these from
[Oracle's Java SE Downloads page](http://www.oracle.com/technetwork/java/javase/downloads): 
[Install Netbeans](https://netbeans.org/community/releases/80/install.html)
[Install Eclipse](http://wiki.eclipse.org/Eclipse/Installation)

### Instructions:

Pull repository contents.
Create a new Java project in your IDE.
Add JAR files to libraries folder of your project.
Include examples of the most typical operation with REST API.
	- Get Configuration
	- Get Accounting
	- Get Exposures
	- Get a FBA Prices
	- Send a market trade
	- Send a limit trade
	- Replace and cancel a limit trade

### Example:

The following Linux shell command executes the 'Java_API' strategy that was previously compiled. The standard output provides the strategy initial status.
```
run:
Attemping to connect to localhost/127.0.0.1 port 8766
Initializing strategy Id:53
List of Trading interfaces available:
Trading InterfaceLMAX_LMAX has index:0 Id:539
List of Prime Brokers available:
Prime Broker:LMAX has index:0 Id:2009
USD asset exposure:20000.0
Prime Broker:Cantor has index:1 Id:2001
USD asset exposure:20000.0
Prime Broker:Velocity has index:2 Id:2004
USD asset exposure:20000.0
Prime Broker:Baxter has index:3 Id:2003
USD asset exposure:20000.0
Prime Broker:FXCM Pro has index:4 Id:2007
USD asset exposure:20000.0
Prime Broker:LCG has index:5 Id:2006
USD asset exposure:20000.0
Prime Broker:FxOne has index:6 Id:2008
USD asset exposure:20000.0
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110248 tiID:539
GBP_USD Top of Book security price:155556 Asset prices: GBP1.411 Global Equity:18139.7669
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110248 tiID:539
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110248 tiID:539
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110248 tiID:539
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110248 tiID:539
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110249 tiID:539
GBP_USD Top of Book security price:155557 Asset prices: GBP1.411 Global Equity:18139.7669
EUR/USD, Top of Book price Ask:110256 tiID:539
EUR/USD, Top of Book price Bid:110249 tiID:539
EUR/USD, Top of Book price Ask:110257 tiID:539
EUR/USD, Top of Book price Bid:110249 tiID:539
EUR/USD, Top of Book price Ask:110257 tiID:539
EUR/USD, Top of Book price Bid:110249 tiID:539
EUR/USD, Top of Book price Ask:110255 tiID:539
EUR/USD, Top of Book price Bid:110249 tiID:539
GBP_USD Top of Book security price:155547 Asset prices: GBP1.41093 Global Equity:18139.7669
Trade sent, GBP_USD Market Id:1 Number of trades Alive1
-> Full book GBP/USD: Quote255 Price: 155552 Liquidity:30000
-> Full book GBP/USD: Quote254 Price: 155553 Liquidity:430000
-> Full book GBP/USD: Quote253 Price: 155554 Liquidity:500000
-> Full book GBP/USD: Quote252 Price: 155555 Liquidity:200000
-> Full book GBP/USD: Quote251 Price: 155556 Liquidity:1000000
-> Full book GBP/USD: Quote250 Price: 155557 Liquidity:1500000
-> Full book GBP/USD: Quote248 Price: 156304 Liquidity:100000
Trade sent, type Limit Id:2 Number of trades Alive:1
Trade modified.
Trade cancell sent:2
BUILD SUCCESSFUL (total time: 31 seconds)
```
