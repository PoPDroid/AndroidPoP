# Integration steps overview:

Add maven jitpack repository in your root build.gradle at the end of repositories: (build.gradle (Project))
https://jitpack.io/#PoPDroid/AndroidPoP/0.3

```java
allprojects {
    repositories {
        jcenter()
        google()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency: (build.gradle (Module))
```java
dependencies {
    ...
    implementation 'com.github.PoPDroid:AndroidPoP:0.3'
}
```

In the source code of the app you wish to protect, identify the event handler on which to add PoP challenge (eg: viewGo.setOnClickListener).
```java
        viewGo = view.findViewById(R.id.send_coins_go);
        viewGo.setOnClickListener(v -> {
```

Define an ID for the return code (eg: LAUNCH_SECOND_ACTIVITY)
```java
    private static final int LAUNCH_SECOND_ACTIVITY = 3;
```

Replace code in the event handler with the PoP challenge code. 
Note: you can define the puzzle depth by modifying the intent extra parameter: "PoPDepth":
```java
        viewGo = view.findViewById(R.id.send_coins_go);
        viewGo.setOnClickListener(v -> {
            //validateReceivingAddress();

            //if (everythingPlausible())
            //    handleGo();
            //else
            //    requestFocusFirst();

            //updateView();
            Intent myint = new Intent(getActivity(), PoPPuzzleChallenge.class);
            myint.putExtra("PoPDepth", 2);
            startActivityForResult(myint, LAUNCH_SECOND_ACTIVITY);
        });
```

Handle onActivityResult event and look for return code defined in PoP ID earlier on (LAUNCH_SECOND_ACTIVITY)
```java
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Boolean result=intent.getBooleanExtra("PoPPuzzle",false);
                if(result){
                    validateReceivingAddress();

                    if (everythingPlausible())
                        handleGo();
                    else
                        requestFocusFirst();

                    updateView();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
```

Make sure result code is a successful one (i.e. returned by PoP activity)

Check that PoP challenge was successful through intent data “PoPPuzzle”

If successful, add original event handler code


# Example integration:
The following project is an example of the PoP challenge integrated into an open source Crypto wallet:
* Original
    * https://github.com/bitcoin-wallet/bitcoin-wallet
* Modified with PoP
    * https://github.com/yonasleguesse/Bitcoin-Wallet

* Video: 

[![](http://img.youtube.com/vi/eq_LDoOGdxk/0.jpg)](http://www.youtube.com/watch?v=eq_LDoOGdxk "PoP in wallet")

The wallet has been modified to include a PoP challenge when the user clicks on the 'Send' button.

The following commit log outlines the changes required when updating the crypto wallet:

https://github.com/yonasleguesse/Bitcoin-Wallet/commit/7d0e8962fc38cc698be8a5a0a53dad96b076818c

Warning: This wallet is for demo purposes. Do not use with your actual crypro funds!


