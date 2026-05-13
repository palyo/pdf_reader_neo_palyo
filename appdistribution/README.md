# Firebase App Distribution

Drop a `service-account.json` next to this file:

```
appdistribution/service-account.json
```

## How to get it

1. Open [Google Cloud Console → IAM → Service Accounts](https://console.cloud.google.com/iam-admin/serviceaccounts) for the Firebase project **`pdf-reader---pdf-viewer-1804d`** (matches `app/google-services.json`).
2. Create (or pick) a service account with the **Firebase App Distribution Admin** role.
3. *Keys → Add key → Create new key → JSON* → save the downloaded file as `appdistribution/service-account.json`.

The `app/build.gradle.kts` checks `if (appDistCredsFile.exists())` before wiring it in, so the build won't break when the file is missing — uploads will just fail until the credential is in place.

## Tester / group configuration

Edit the `defaultAppDistTesters` / `defaultAppDistGroups` strings at the top of `app/build.gradle.kts`, **or** override per-invocation:

```bash
# Comma-separated emails:
./gradlew uploadReleaseToFirebase \
    -PfirebaseAppDistributionTesters=alice@example.com,bob@example.com

# Or via env var:
FIREBASE_APP_DISTRIBUTION_GROUPS=qa-team ./gradlew uploadReleaseToFirebase
```

Group aliases must already exist in **Firebase Console → App Distribution → Groups**, or the upload returns `[404] Requested entity was not found.`

## Release signing

The release build is signed with `pdf_reader.jks` at the project root. Defaults assume `alias = pdf_reader`, store/key passwords = `pdf_reader`. Override via:

```bash
RELEASE_STORE_PASSWORD=… RELEASE_KEY_ALIAS=… RELEASE_KEY_PASSWORD=… \
    ./gradlew uploadReleaseToFirebase
```

…or set them in `~/.gradle/gradle.properties`:

```properties
RELEASE_STORE_PASSWORD=…
RELEASE_KEY_ALIAS=…
RELEASE_KEY_PASSWORD=…
```

## Uploading

The artifact type is **AAB** (Android App Bundle). This requires the Firebase
project to be linked to a Google Play account — *Firebase Console → Project
Settings → Integrations → Google Play → Link*. If you haven't linked yet,
upload will fail with a Play-association error. To revert to APK uploads
temporarily, change `artifactType = "AAB"` back to `"APK"` in
`app/build.gradle.kts` and swap `bundleRelease` for `assembleRelease` below.

The full pipeline (build + upload) is registered as `uploadReleaseToFirebase`:

```bash
./gradlew uploadReleaseToFirebase
```

Or run the steps explicitly (matches `appdistribution/command`):

```bash
./gradlew bundleRelease appDistributionUploadRelease
```
