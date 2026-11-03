# NxDroidLib

Gemeinsame Android-Bibliothek für die NxDroid-Apps: die Java-Bibliothek **jlib** samt JSON, die Android-Helfer **NxDroid**, der **NWebClient** und das **NWeb-Konto** (Zugangsdaten speichern, bearbeiten und aus der App NxDroid importieren).

Genutzt von:

| App | Pfad | Build |
|---|---|---|
| NxMap | `D:\dev\NxMap` | AGP 9.1, AndroidX, Kotlin |
| arscanner | `D:\VCS\arscanner` | AGP 9.1, AndroidX, Java (ab Branch `androidx-migration`) |

Die Bibliothek setzt **AndroidX** voraus und bringt `androidx.core` als Abhängigkeit mit. Apps mit der alten Support Library können sie nicht einbinden.

## Inhalt

| Paket / Klasse | Aufgabe |
|---|---|
| `net.bsnx.jlib.*` | Allgemeine Java-Bibliothek: Collections, IO, Netz, Text, XML, SQL, kleiner HTTP-Server, Grafik (`Vec2d`, `Vec3d`, `Vec3dList`) |
| `net.bsnx.json.*` | JSON/XML-Parser (`JSONObject`, `JSONArray`, …) |
| `net.bsnx.data.*`, `net.bsnx.i.*` | Datenzugriff und gemeinsame Interfaces |
| `net.bsnx.Java`, `Php`, `StaticFunctions` | Hilfsfunktionen |
| `net.bsnx.NWebClient` | Client für den NWeb-Server |
| `net.bsnx.nxdroid.NxDroid` | Android-Helfer: Einstellungen (`getPrefString` …), `wget`, `openUrl`, Teilen, UDP, `client(ctx)` für einen `NWebClient` mit den gespeicherten Zugangsdaten |
| `net.bsnx.nxdroid.GeoItem`, `net.bsnx.nxdroid.ui.*` | Geo-Objekt und einfache UI-Bausteine (`VBox`, `HBox`, `PrefBool`, `PrefTextEdit` …) |
| `net.bsnx.nxdroidlib.NWebAccount` | Zugriff auf die Zugangsdaten: `getUser`, `getUrl`, `getPass`, `save`, `isConfigured`, `summary`, `openSettings` |
| `net.bsnx.nxdroidlib.NWebAccountActivity` | Seite zum Bearbeiten und zum Import aus NxDroid |

Die Paketnamen sind dieselben wie vorher in den Apps. Imports in den Apps mussten deshalb nicht geändert werden.

`NxDroid.shareFile` und `NxDroid.shareImage` verwenden fest die FileProvider-Authority `net.bsnx.nxdroid2.provider`. Die gibt es nur in der App NxDroid, in anderen Apps funktionieren diese beiden Methoden so nicht.

### Speicherort

Die Daten liegen in den SharedPreferences **`nxdroid`** unter den Schlüsseln `nweb.user`, `nweb.url` und `nweb.pass`. Das sind dieselben Schlüssel wie bisher in NxMap, vorhandene Einstellungen bleiben also erhalten. Code wie `NxDroid.getPrefString(ctx, "nweb.user", "")` funktioniert weiter.

`getUrl()` liefert `https://bsnx.net/4.0/`, wenn keine URL gespeichert oder das Feld leer ist.

### Import aus NxDroid

„Von NxDroid importieren“ startet `net.bsnx.nxdroid.SettingActivity` in der App `net.bsnx.nxdroid2`. Dabei wird `response_class` = `net.bsnx.nxdroidlib.NWebAccountActivity` und `response_package` = Paket der aufrufenden App mitgegeben. NxDroid startet die Seite danach mit den Extras `nweb.user`, `nweb.url` und `nweb.pass`.

Die Seite ist dafür `exported`. Damit nicht jede beliebige App heimlich Zugangsdaten oder eine fremde Server-URL unterschieben kann, werden importierte Daten **erst nach Bestätigung** in einem Dialog übernommen. Der Dialog zeigt Benutzer und URL an.

## Einbinden

Die Bibliothek wird als Unterprojekt eingebunden. Es gibt keinen eigenen Gradle-Wrapper, gebaut wird sie immer mit dem Android-Gradle-Plugin der jeweiligen App. `lib/build.gradle` ist mit AGP 9.1 getestet.

### Groovy (`settings.gradle`, z. B. arscanner)

```groovy
include ':nxdroidlib'
project(':nxdroidlib').projectDir = new File(settingsDir, '../../dev/NxDroidLib/lib')
```

```groovy
// app/build.gradle
dependencies {
    implementation project(':nxdroidlib')
}
```

### Kotlin DSL (`settings.gradle.kts`, z. B. NxMap)

```kotlin
include(":nxdroidlib")
project(":nxdroidlib").projectDir = file("../NxDroidLib/lib")
```

Werden die Plugins über einen Version Catalog verwaltet, muss `com.android.library` im Root-Projekt bekannt sein:

```toml
# gradle/libs.versions.toml
android-library = { id = "com.android.library", version.ref = "agp" }
```

```kotlin
// build.gradle.kts (Root)
plugins {
    alias(libs.plugins.android.library) apply false
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(project(":nxdroidlib"))
}
```

Der Pfad in `settings` ist relativ zum jeweiligen Projekt. Liegen die Projekte anders, muss er angepasst werden.

## Verwendung

```java
// Einstellungsseite öffnen
startActivity(NWebAccount.openSettings(context));

// Kurzinfo für eine Einstellungsliste: "max @ bsnx.net" oder "Nicht eingerichtet"
String summary = NWebAccount.summary(context);

// Zugangsdaten lesen
if (NWebAccount.isConfigured(context)) {
    NWebClient client = new NWebClient(NWebAccount.getUrl(context),
            NWebAccount.getUser(context), NWebAccount.getPass(context));
}
```

In einer AndroidX-`PreferenceScreen` (NxMap):

```kotlin
categoryAccount.addPreference(Preference(context).apply {
    key = "nweb.account"
    title = "NWeb-Konto"
    summary = NWebAccount.summary(context)
    intent = NWebAccount.openSettings(context)
})
```

## Hinweise

- **minSdk 26, compileSdk 34.** `androidx.core` 1.13 verlangt mindestens compileSdk 34. Die Ressourcen der Bibliothek werden gegen die compileSdk der jeweiligen App gelinkt, die App braucht also ebenfalls mindestens 34.
- **Java 11** als Sprachstand, ohne Kotlin.
