# Stardust Notepad
Ez a dokumentáció a BMSzC Petrik Lajos Két Tanítási Nyelvű Technikumi képzésem során készített csoportos vizsgaremek Mobil alkalmazás Kliensének bemutatására szolgál. Az alkalmazás egy cross-platform jegyzet kezelő, három Klienssel és egy közös szerver oldali Backend-del.

## Telepités
### ⚠ Az alkalmazás működéséhez szükséges a <a href="https://github.com/blaiseludvig/stardust-backend">*Backend*</a> futtatása.
### Mielőtt az Android Studio-ban buildelnénk, változtassuk meg a szerver IP címét és a port számát a konstansok között.
```kotlin
object Constans {
    var BASE_URL = "http://192.168.0.14:3000" // A szerver IP címe és port száma
    var USER_TOKEN = "user_token"
}
```

## Az alkalmazás
#### Az alkalmazás frontendjének fejlesztése során az alábbi technológiákat használtam:

+ Kotlin: A Kotlin egy modern, funkcionális és objektumorientált programozási nyelv, amely a Java platformra épül.

+ Lifecycle: A Lifecycle a szoftverkomponensek vagy alkalmazások állapotváltozásait írja le az indulástól a bezárásig.

+ Retrofit2: A Retrofit2 egy Android és Java HTTP kliens könyvtár, amely egyszerűvé teszi az API hívásokat, a RESTful webszolgáltatások használatát és a válaszok feldolgozását.

+ OkHttp3: Az OkHttp3 egy Java és Android HTTP kliens könyvtár, amely lehetővé teszi a HTTP/2 és a WebSocket protokollok támogatását, valamint az egyszerű és hatékony HTTP kérések és válaszok kezelését.

+ Lottie: A Lottie egy Android és iOS számára elérhető, natív animáció lejátszó könyvtár, amely lehetővé teszi a JSON formátumú, tervezők által készített After Effects animációk egyszerű használatát.

+ Material Design: A Material Design egy modern és intuitív dizájnnyelv, amelyet a Google fejlesztett ki, és amely elősegíti az összhangot az alkalmazások között, valamint lehetővé teszi a könnyű és egyértelmű navigációt és az információ szemléletes megjelenítését különböző platformokon.

+ Robohash: A Robohash egy olyan webes szolgáltatás, amely egy adott szöveges bevitel (itt email cím) alapján generál egyedi robotképeket. Ezt a profilkép generáláshoz használtam.

+ Picasso: Ez egy közkedvelt, nyílt forráskódú képfeldolgozási könyvtár Android alkalmazásokhoz. Ezzel URL címet könnyen tudunk beállítani a kép forrásának.

### Az alkalmazáshoz egy rövid gyorstalpalót is készítettem amit [*itt*](https://www.canva.com/design/DAFhOxpbCZE/hvtPHXAKnPlAy-KXe9UMlw/view?website#2:stardust-notepad) lehet megtekinteni
