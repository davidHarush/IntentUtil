# IntentUtil

![GitHub release (latest by date)](https://img.shields.io/github/v/release/davidHarush/IntentUtil)
![GitHub top language](https://img.shields.io/github/languages/top/davidHarush/IntentUtil)
![GitHub repo size](https://img.shields.io/github/repo-size/davidHarush/IntentUtil)
![GitHub issues](https://img.shields.io/github/issues/davidHarush/IntentUtil)

## Installation.
Step 1. Add the JitPack repository to your build file. <br>
Add it in your root build.gradle at the end of repositories:
```sh
allprojects {
  repositories {
	  ...
	  maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency<br>
```sh
dependencies {
    implementation 'com.github.davidHarush:IntentUtil:1.01'
}

```
For more info :<br>
[![](https://jitpack.io/v/davidHarush/IntentUtil.svg)](https://jitpack.io/#davidHarush/IntentUtil)
<br>
<br>


## How to use it.

Send sms
```sh
IntentUtil(CustomIntents.Sms(to = "+8758558555" , msg = text)).start(<Fragment|Activity>)

```

Send Email
```sh
IntentUtil(CustomIntents.Email(address = "aaaa@Gmail.com", subject = title)).start(<Fragment|Activity>)
```

Do GoogleSearch 
```sh
IntentUtil(CustomIntents.GoogleSearch(query = "Android wiki")).start(<Fragment|Activity>)
```

And many other built in Intents..




