##Secure Payment via Mobile Phone
A prototype for an E2E mobile app with secure payment. Refer report for more details on security.
Below is a detailed instruction you could follow without any prior exposure to mobile app development. 

###Overview:

1. The system consists of 3 components: Android app running on a mobile device, Login Server and Payment Server.
2. LoginServer project is run in eclipse in laptop (server1).
3. LoginServerPayment project is run in eclipse in the same laptop(server2).
4. MobilePaymentApp is run on a android OS mobile device.
5. The user can register by giving the required details on server1.
6. The user login by providing username and password to server1.
7. SSL is implemented between user and server1. Username is sent unencrypted and password hashed.
8. Server1 returns the login success/failure.
9. User navigates to shopping on success and selects the product to buy.
10. User then provides the card details and is sent to server2 aes encrypted.
11. Server2 decrypts the cipeher.
12. The information entered in mobile device and displayed at server2 are compared manually.
13. TestAESExample project is used to implement the brute force attack.
14. TestDictionary project is used to implement the dictionary attack.
15. Security mechanisms implemented: Double hashing with salt, SSL and AES encryption.


###Steps to import Login Server:

1. In eclipse, select import from file
2. Select Existing project into workspace
3. Opt for Select archive file
4. Browse LoginServer.zip
5. Click finish to import

###Steps to import Payment Server:

1. In eclipese, select import from file
2. Select Existing project into workspace
3. Opt for Select archive file
4. Browse LoginServerPayment.zip
5. Click finish to import

###Steps to import and install MobilePayment app:

1. Refer the following link to download and setup android studio: 
   http://developer.android.com/training/basics/firstapp/index.html
2. Import the MobilePaymentApp project onto studio (You may have to unzip and import the folder MobilePaymentApp)
3. Refer the following link to install apk (android app) into a handheld device like tablet.
   http://developer.android.com/tools/device.html
4. Once the app is successfully installed on android device unplug the usb cable

###Steps to run the project:

1. Run the Login Server using eclipse in a laptop.
2. Open the MobilePaymentApp installed in a tablet.
3. Click on register button.
4. Enter the registration details.
5. Click on the REGISTER button.
6. Enter the login details.
7. Click on the LOGIN button.
8. Click on buy button.
9. Stop the Login Server in eclipse.
10. Start the Payment Server in eclipse. 
11. Enter the card details.
12. Click on Pay button.
13. Click on Logout button.
