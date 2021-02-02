# Huobi withdrawal 
Showing how an attack can target different types of 2FA (sms,google auth, email) 

* Video: 

[![](http://img.youtube.com/vi/7rKeRlfDrkw/0.jpg)](http://www.youtube.com/watch?v=7rKeRlfDrkw "Huobi Withdraw")

* Attack steps:
  * acc service opens huobi 00:04
  * clicks on bitcoin wallet  00:07
  * clicks on withdraw 00:10
  * populates attackers address 00:10
  * selects ALL in 'amount' (i.e. empties all available funds) 00:12
  * clicks on withdraw 00:13
  * clicks on confirm that you understand danger :) 00:15
  * asks security page to send out email and SMS codes 00:16
  * opens Google auth and steals 2fa code 00:18
  * opens Gmail and steals 2fa code 00:24
  * opens Messaging app and steals 2FA code 00:25
  * opens huobi again and populates stolen codes 00:34
  * confirms transaction 00:37
* Tested on 
  * Device: Samsung Galaxy S8
  * Android version: 9
  * App version: 6.1.1

# Coinbase Withdrawal 
Showing how an attack can be launched remotely (integrated in metasploit) and in a covert manner with an overlay when launching - as reported to coinbase

* Video: 

[![](http://img.youtube.com/vi/MLHUin479kI/0.jpg)](http://www.youtube.com/watch?v=MLHUin479kI "Coinbase Withdraw")

* Attack steps:
  * Victim has euro 9.95 worth of BTC in their wallet 00:06
  * Attacker launches attack remotely. The attack tells the trojan to empty the victim’s bitcoin wallet on coinbase, and send the funds to the attacker’s device 00:37
  * The trojan launches the attack and presents an overlay covering the attack from the victim 00:37
  * The funds are emptied from the wallet 00:50
  * The transaction is confirmed and pending transmission 00:59
  * Euro 5 worth of bitcoin was sent to the attacker’s address 01:09
* Tested on 
  * Device: Nexus 5x
  * Android version: 7
  * App version: 6.55.0

# Binance withdrawal 
Showing how an attack can be launched remotely (integrated in metasploit) and in a covert manner with an overlay when launching. Also Showing how an attacker can steal 2FA and integrate the stolen token into the attack - as reported to binance

* Video: 

[![](http://img.youtube.com/vi/NE4rX27nQ8A/0.jpg)](http://www.youtube.com/watch?v=NE4rX27nQ8A "Binance Withdraw")

* Attack steps:
  * Attacker starts by remotely commanding the accessibility trojan to steal the current 2FA code from the google authenticator app 00:05
  * The 2FA code is sent to the attacker (657053) 00:09
  * The attacker launches the attack on binance, telling the accessibility trojan to withdraw bitcoin funds to the attacker’s wallet, also passing the 2FA code that was just stolen 00:16
  * The attack is launched, and covered using an overlay 00:18
  * The attacker launches another attack that open’s the victim’s email app, and steals the withdrawal confirmation link that was sent by binance 00:38
  * The confirmation link is sent to the attacker 00:41
  * The attacker opens the confirmation link remotely, and this confirms the transaction 00:51
  * The transaction is confirmed 00:54
  * The confirmation email is deleted to further hide the transaction 01:04
* Tested on 
  * Device: Nexus 5x
  * Android version: 7
  * App version: 1.21.1

# Binance wallet whitelist attack 
Forcing the addition of the attacker’s wallet to the whitelisted addresses - still vulnerable even after binance patched. This could be even more of a justification for PoP

* Video: 

[![](http://img.youtube.com/vi/W_RUHK31UCw/0.jpg)](http://www.youtube.com/watch?v=W_RUHK31UCw "Binance Whitelist")

* Attack steps:
  * Accessibility trojan opens binance 00:04
  * Accessibility trojan clicks menu 00:05
  * Accessibility trojan clicks settings 00:06
  * Accessibility trojan clicks withdrawal addresses 00:06
  * Accessibility trojan clicks add new addresses 00:06
  * Accessibility trojan clicks select coin 00:07
  * Accessibility trojan selects BTC 00:08
  * Accessibility trojan adds whitelisted address 00:11
  * Accessibility trojan clicks save wallet 00:13
  * Binance asks for email verification and authenticator app codes 00:13
  * Accessibility trojan clicks Send email verification code 00:13
  * Accessibility trojan opens google authenticator and steals token 00:14
  * Accessibility trojan opens binance and pastes code 00:18
  * Accessibility trojan opens gmail and steals token 00:19
  * Accessibility trojan opens binance and pastes code 00:22
  * Accessibility trojan clicks submit 00:22
  * Binance confirms new wallet was whitelisted 00:24
* Tested on 
  * Device: Samsung Galaxy S8
  * Android version: 9
  * App version: 1.35.2

# Coinbase pin unlock 
Showing how an accessibility service can force unlock the victim app app

* Video: 

[![](http://img.youtube.com/vi/vmZNzONYhlU/0.jpg)](http://www.youtube.com/watch?v=vmZNzONYhlU "Coinbase Unlock")

* Attack steps:
  * Accessibility trojan opens coinbase app and enters pin 
* Tested on 
  * Device: Nexus 5x
  * Android version: 7
  * App version: 6.55.0

# Binance pattern unlock 
Showing how an accessibility service can force unlock the app

* Video: 

[![](http://img.youtube.com/vi/5XTwU_9tqHw/0.jpg)](http://www.youtube.com/watch?v=5XTwU_9tqHw "Binance Unlock")

* Attack steps:
  * Accessibility trojan opens Binance app and swipes unlock pattern
* Tested on 
  * Device: Samsung Galaxy S8
  * Android version: 9
  * App version: 1.35.2

# Huobi pin unlock 
Showing how an accessibility service can force unlock the app by for

* Video: 

[![](http://img.youtube.com/vi/8Yz0Ugb6oUw/0.jpg)](http://www.youtube.com/watch?v=8Yz0Ugb6oUw "Huobi unlock")

* Attack steps:
  * Accessibility trojan opens Huobi app and swipes unlock pattern
* Tested on 
  * Device: Samsung Galaxy S8
  * Android version: 9
  * App version: 6.1.1
