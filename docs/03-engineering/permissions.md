# Miozira — Permissions Specification

**Document:** `permissions.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Architecture:** Offline-first, privacy-minimal  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the permission strategy for Miozira Prototype 0.1.

It specifies:

- which Android permissions are required;
- which permissions are explicitly not required;
- when and how microphone permission is requested;
- parent-facing explanation;
- denial behavior;
- “don’t ask again” behavior;
- permission revocation behavior;
- child-flow fallback;
- manifest minimization;
- testing requirements;
- future iPadOS mapping.

The objective is:

> **request the minimum possible permissions, only when necessary, with the adult in control.**

---

# 2. Permission Philosophy

Miozira should follow these rules:

1. **Least privilege** — do not request a permission unless Prototype 0.1 genuinely needs it.
2. **Adult control** — sensitive permission decisions belong in the parent area/onboarding, not child mode.
3. **Just-in-time** — explain before requesting.
4. **Graceful denial** — denial must not break the core child learning loop.
5. **No nagging** — repeated prompts are prohibited.
6. **No hidden coupling** — permissions must not be required indirectly by unnecessary dependencies.

---

# 3. Required Runtime Permission

Prototype 0.1 has exactly one sensitive runtime permission:

> **Microphone**

Android permission:

```text
android.permission.RECORD_AUDIO
```

It is used for:

- optional child speaking-attempt detection;
- optional parent/family voice recording.

---

# 4. Microphone Is Optional for Core Learning

The app must remain fully usable for the core learning loop without microphone access.

Without microphone:

```text
tap picture
→ hear canonical word
→ short pause
→ gentle response
→ continue
```

Therefore microphone permission must not be treated as a prerequisite to starting Miozira.

---

# 5. Microphone Use Cases

The same Android permission supports two distinct product features:

## Child speaking-attempt detection

- short 2–3 second capture windows;
- transient PCM only;
- no saved audio;
- no transcription;
- no pronunciation score.

## Parent family recording

- adult-initiated;
- creates an app-private recording file;
- can be previewed, replaced, and deleted.

The permission is shared, but the application services remain separate.

---

# 6. Permission Request Ownership

Only adult-facing flows should initiate microphone permission requests.

Allowed places:

- initial parent onboarding;
- parent settings;
- family recording flow after adult intent.

Not allowed:

- child taps concept;
- child enters LISTENING state;
- app launch before explanation;
- background service.

---

# 7. Parent Explanation Before System Dialog

Before Android shows the system microphone dialog, Miozira must present plain-language explanation.

Recommended copy:

> **Microphone**
>
> Miozira can briefly listen to check whether your child tried to speak. It does not grade pronunciation, transcribe speech, save ordinary child speaking attempts, or upload them.
>
> The microphone is also used if you choose to record a family voice for a word.
>
> Miozira still works if you leave this off.

Actions:

```text
Enable microphone
Not now
```

---

# 8. Explanation Requirements

The explanation must state:

- what microphone is used for;
- what it is not used for;
- that ordinary child attempt audio is not saved;
- that it is optional;
- that the app still works without it.

Do not hide these facts behind a generic privacy-policy link.

---

# 9. First-Run Flow

Recommended flow:

```text
Parent onboarding
→ Explain microphone
→ Parent chooses Enable / Not now
```

If:

```text
Enable
```

then invoke Android system permission request.

If:

```text
Not now
```

do not request system permission.

---

# 10. Permission States

Internal permission model:

```text
NOT_DETERMINED
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
UNAVAILABLE
```

Exact platform detection may map Android APIs into these semantic states.

---

# 11. GRANTED Behavior

If microphone permission is granted:

- child attempt detection may be enabled if parent setting is on;
- family recording is available;
- microphone still opens only during explicit short windows.

Grant does not mean continuous access.

---

# 12. DENIED Behavior

If parent denies permission:

- save parent preference as disabled/not enabled;
- do not block setup;
- do not repeatedly prompt;
- child loop continues without listening window.

Parent settings may later offer:

> Enable microphone

if the adult chooses.

---

# 13. DENIED_DONT_ASK_AGAIN Behavior

If Android will no longer show the permission dialog directly:

Parent settings may show:

> “Microphone access is turned off in Android settings.”

Optional action:

```text
Open Android settings
```

Only surface this if the parent deliberately tries to enable the feature.

Do not redirect to settings automatically.

---

# 14. Revoked After Grant

If permission was previously granted but later revoked in Android settings:

- detect capability failure;
- turn off microphone-dependent behavior;
- child learning continues;
- parent settings should reflect actual permission state.

Do not crash or loop on permission requests.

---

# 15. Parent Setting vs System Permission

These are separate states.

Example:

```text
system permission = GRANTED
parent feature setting = OFF
```

Expected:

> microphone is not opened.

The parent feature toggle is an additional product-level gate.

---

# 16. Child Attempt Measurement Semantics

If parent feature is OFF:

```text
attempt_state = NOT_MEASURED
```

If parent feature is ON but permission/hardware unavailable:

```text
attempt_state = MICROPHONE_UNAVAILABLE
```

If a valid listening window completes with no detected attempt:

```text
attempt_state = NO_ATTEMPT_DETECTED
```

---

# 17. Permission Prompt Timing

Do not request microphone permission:

- immediately on first app launch before explanation;
- every time child session begins;
- every time family recording screen opens.

Request only when adult intent is clear.

---

# 18. One Permission, No Separate Child Permission

There is no second Android permission specifically for child attempt detection.

The app must handle the shared microphone permission responsibly.

---

# 19. Camera Permission

Prototype 0.1 does **not** use camera.

Do not declare/request:

```text
android.permission.CAMERA
```

No feature requires:

- taking pictures;
- scanning;
- video;
- face detection.

---

# 20. Location Permissions

Prototype 0.1 does **not** use location.

Do not declare/request:

```text
ACCESS_FINE_LOCATION
ACCESS_COARSE_LOCATION
ACCESS_BACKGROUND_LOCATION
```

No feature requires location.

---

# 21. Contacts Permission

Prototype 0.1 does **not** use contacts.

Do not request:

```text
READ_CONTACTS
WRITE_CONTACTS
GET_ACCOUNTS
```

---

# 22. Phone / SMS Permissions

Prototype 0.1 does not need:

```text
READ_PHONE_STATE
CALL_PHONE
READ_SMS
SEND_SMS
```

---

# 23. Calendar Permissions

Prototype 0.1 does not need calendar access.

No reminder scheduling is part of the vertical slice.

---

# 24. Notification Permission

Prototype 0.1 does not use push/local notifications.

Do not request Android notification permission.

The product has:

- no streak reminders;
- no daily obligation;
- no retention nudges.

---

# 25. Storage Permissions

Prototype 0.1 stores data in app-private storage.

Do not request legacy broad storage permissions such as:

```text
READ_EXTERNAL_STORAGE
WRITE_EXTERNAL_STORAGE
MANAGE_EXTERNAL_STORAGE
```

---

# 26. Photos / Media Permissions

No need for media-library permissions.

Do not request access to:

- photos;
- videos;
- music/audio collections.

Family recordings remain private app files.

---

# 27. Bluetooth Permissions

Prototype 0.1 does not scan, pair, or manage Bluetooth devices.

Do not request Bluetooth permissions merely to allow normal system audio routing.

If a future platform requirement emerges for a specific audio feature, it must be documented separately.

---

# 28. Nearby Devices

No nearby-device discovery is required.

Do not request:

```text
BLUETOOTH_SCAN
BLUETOOTH_CONNECT
NEARBY_WIFI_DEVICES
```

for Prototype 0.1 unless a concrete Android API requirement is proven.

---

# 29. Internet Permission

Prototype 0.1 should aim to omit:

```text
android.permission.INTERNET
```

because:

- canonical content is bundled;
- database is local;
- no analytics;
- no backend;
- no cloud speech;
- no sync.

If a dependency injects this permission through manifest merging, review it.

---

# 30. Network State Permissions

No need for:

```text
ACCESS_NETWORK_STATE
CHANGE_NETWORK_STATE
```

unless a specific build/dependency reason exists.

The child loop does not branch on network state.

---

# 31. Wake Lock

Prototype 0.1 should not require:

```text
WAKE_LOCK
```

by default.

If later used to keep an active session awake, that decision must be explicitly reviewed.

Prefer normal screen behavior first.

---

# 32. Foreground Service Permissions

No persistent foreground service is required.

Do not request foreground-service-specific permissions for the normal learning loop.

---

# 33. Vibration Permission

Modern Android haptic APIs may not require a special dangerous permission for ordinary haptic feedback.

Miozira does not require vibration for core functionality.

Avoid adding permission complexity for optional haptics.

---

# 34. Biometric Permissions

No biometric authentication is used.

No:

- fingerprint;
- face;
- voice biometrics.

---

# 35. Accessibility Service Permission

Miozira is not an accessibility service.

It should support TalkBack/semantics, but must not request accessibility-service privileges.

---

# 36. Exact Android Manifest Intent

Prototype 0.1 manifest should contain as few sensitive declarations as possible.

Expected sensitive permission set:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

Potentially nothing else beyond ordinary non-sensitive framework/system declarations.

---

# 37. Microphone Hardware Feature Declaration

Because microphone is optional for core learning, do not make microphone hardware installation-blocking if avoidable.

If declaring feature:

```xml
<uses-feature
    android:name="android.hardware.microphone"
    android:required="false" />
```

This communicates optional capability.

Final manifest should be validated against actual build output.

---

# 38. Camera Feature Declaration

No camera feature requirement.

Do not accidentally inherit camera requirements through dependencies.

---

# 39. Manifest Merge Review

Before release/family test, inspect the final merged Android manifest.

Check for unexpected permissions added by libraries.

Reject unexplained additions.

---

# 40. Dependency Permission Audit

For every dependency, review whether it introduces:

- INTERNET;
- advertising ID;
- storage;
- Bluetooth;
- notifications;
- location;
- camera;
- analytics.

Prototype 0.1 should prefer libraries that do not broaden permissions unnecessarily.

---

# 41. Permission Rationale in Parent UI

Parent copy should use product language, not Android implementation language.

Good:

> “Miozira briefly listens to see if your child tried to speak.”

Avoid:

> “Required for RECORD_AUDIO processing pipeline.”

---

# 42. No Child Permission Copy

The child should not see:

- “permission denied”;
- “microphone access required”;
- “go to settings.”

Child mode simply continues without listening.

---

# 43. Family Recording Permission Flow

If parent chooses to record a family voice and microphone is not granted:

1. show microphone explanation;
2. request permission if adult confirms;
3. if denied, stay in parent UI;
4. standard canonical voice remains available.

---

# 44. No Permission Before Need

Do not request microphone solely because family recording *might* be used someday.

The initial onboarding may offer it because child attempt detection is an active feature, but parent can choose “Not now.”

---

# 45. Permission Re-Prompt Rule

After a denial:

- do not repeatedly prompt automatically.

A new system request should occur only after a later explicit adult action such as:

> Enable microphone

---

# 46. Permission State Persistence

Product preference may remember:

```text
speech_attempt_detection_enabled
```

But system permission state must always be queried from the OS when needed.

Do not assume stored preference means permission still exists.

---

# 47. Permission and Backup

Do not treat restored preferences as proof of restored permission.

After reinstall/restore:

- query actual system permission;
- reconcile feature setting.

---

# 48. Permission and Reset

Parent data reset should decide whether to reset:

```text
speech_attempt_detection_enabled
```

Recommended:

> return product preference to default/off or onboarding state.

System Android permission itself cannot be revoked programmatically by Miozira.

---

# 49. Permission and App Uninstall

Uninstall removes app state according to OS behavior.

On reinstall:

- permission state may need to be requested again according to Android behavior;
- Miozira should not assume prior consent.

---

# 50. Permission and Process Death

Permission state is OS-owned.

No special persistence needed beyond product preferences.

---

# 51. Permission and Backgrounding

If app backgrounds during active microphone capture:

- stop capture immediately;
- permission remains granted;
- do not continue recording in background.

---

# 52. Permission and Screen Lock

If screen locks during listening/recording:

- stop capture;
- preserve safe interaction state;
- do not create a false no-attempt result from interruption.

---

# 53. Permission and Parent Gate

Entering parent mode should stop any child microphone window.

Permission grant remains unchanged.

---

# 54. Permission Error Types

Recommended semantic errors:

```text
PermissionDenied
PermissionPermanentlyDenied
PermissionUnavailable
PermissionRequestFailed
```

Do not leak raw platform exceptions to UI.

---

# 55. Parent Error Copy

Examples:

### Denied

> “Microphone access is off. Miozira still works without it.”

### Settings required

> “Microphone access is blocked in Android settings. You can turn it on there if you want to use speaking detection or family recordings.”

---

# 56. Privacy Alignment

Permission explanation must match actual implementation.

If copy says:

> “ordinary child speaking attempts are not saved”

then code must ensure:

- no temp file;
- no cache file;
- no debug recording.

---

# 57. Google Play Data Safety Alignment

If distributed through Google Play, Data Safety disclosures must match:

- microphone use;
- local processing;
- no child raw audio upload;
- no analytics/backend collection.

Permission declarations and store disclosures must not contradict each other.

---

# 58. Google Play Families Alignment

Because the product is child-directed, microphone use deserves heightened review.

The app should be able to explain:

- why microphone is used;
- that it is optional;
- that ordinary child attempt audio is not stored;
- that family recordings are parent-created and local.

---

# 59. No Advertising Identifier

Prototype 0.1 does not need advertising ID access.

No ads SDK.

---

# 60. No Device Identifier Collection

Do not collect persistent hardware/device identifiers for analytics or profile purposes.

---

# 61. No Account Permission Flow

No sign-in/account permission.

No OAuth.

No Google sign-in.

No Apple sign-in.

---

# 62. Android Permission Controller

Recommended service:

```text
PermissionService
```

Responsibilities:

```text
getMicrophonePermissionState()
requestMicrophonePermission()
openMicrophoneSettings()
```

Domain logic should not call Android permission APIs directly.

---

# 63. Controller Flow Example — First Enable

```text
Parent taps Enable microphone
→ PermissionService checks state
→ NOT_DETERMINED
→ Android system dialog
→ GRANTED
→ setting enabled
```

---

# 64. Controller Flow Example — Denied

```text
Parent taps Enable microphone
→ Android dialog
→ DENIED
→ setting remains off
→ parent sees plain-language state
```

---

# 65. Controller Flow Example — Permanently Denied

```text
Parent taps Enable microphone
→ PermissionService returns DENIED_DONT_ASK_AGAIN
→ parent sees Open Android settings option
```

No repeated system dialog attempt.

---

# 66. Controller Flow Example — Child Session

```text
interaction reaches speaking opportunity
→ parent setting checked
→ OS permission checked
→ if both enabled/granted:
      start SpeechAttemptDetector
   else:
      skip measurement
```

---

# 67. Permission Decision Table

| Product setting | System permission | Child attempt behavior |
|---|---|---|
| Off | Any | `NOT_MEASURED` |
| On | Granted | Open short listening window |
| On | Denied | `MICROPHONE_UNAVAILABLE` |
| On | Permanently denied | `MICROPHONE_UNAVAILABLE` |
| On | Hardware unavailable | `MICROPHONE_UNAVAILABLE` |

---

# 68. Parent Recording Decision Table

| System permission | Family recording behavior |
|---|---|
| Granted | Recording available |
| Not determined | Explain → request if parent agrees |
| Denied | Recording unavailable; canonical voice remains |
| Permanently denied | Offer Android settings link |
| Hardware unavailable | Show parent-facing unavailable message |

---

# 69. Android Permission Testing

Test on current Android target:

1. fresh install;
2. grant;
3. deny;
4. deny repeatedly / permanent denial behavior;
5. revoke from settings;
6. grant from settings;
7. app restart after each state;
8. child session with setting off;
9. child session with setting on + denied;
10. family recording flow with denied permission.

---

# 70. Minimum-SDK Permission Testing

Permission behavior should also be validated at the selected minimum Android API level.

Do not assume the latest Android dialog flow exactly matches older supported versions.

---

# 71. Permission UI Accessibility

Parent permission explanation must:

- support TalkBack;
- support text scaling;
- use explicit button labels;
- not rely on color alone.

---

# 72. No Custom Fake Permission Dialog

Do not imitate the Android system permission dialog visually.

Miozira may show explanation first, then use the real system request.

---

# 73. No Dark Pattern

Do not:

- hide “Not now”;
- use guilt language;
- imply child learning will fail;
- repeatedly redirect to settings.

---

# 74. Consent Is Feature-Level, Not Legal Checkbox Theater

Prototype 0.1 does not need a giant consent wall.

It needs a clear adult choice for the microphone feature.

---

# 75. Permission Diagnostics

Development builds may show:

```text
system microphone permission
product microphone setting
microphone hardware capability
```

This is useful for debugging.

Do not expose technical diagnostics in child mode.

---

# 76. Unexpected Permission Detection

As part of CI/release QA, compare merged manifest against an approved permission allowlist.

Expected allowlist:

```text
RECORD_AUDIO
```

plus any explicitly reviewed non-sensitive/system declarations.

If a new dangerous permission appears:

> fail review.

---

# 77. Permission Allowlist File

Recommended future repository artifact:

```text
config/android-permission-allowlist.txt
```

or equivalent test assertion.

This is optional but useful.

---

# 78. Build Variant Consideration

Debug tooling must not accidentally introduce production permissions.

If a debug-only tool adds network/storage permission:

- keep it isolated to debug;
- verify release manifest separately.

---

# 79. iPadOS Mapping

Future iPadOS will require microphone usage description in the app's privacy configuration.

The same product rule applies:

- adult explanation;
- system permission;
- optional feature;
- graceful denial.

Exact Apple keys belong in the future iOS platform implementation.

---

# 80. Cross-Platform Permission Abstraction

Shared code should depend on semantic states:

```text
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
NOT_DETERMINED
UNAVAILABLE
```

Platform adapters translate Android/iOS specifics.

---

# 81. Permissions Not Required Summary

Prototype 0.1 should not request:

```text
Camera
Location
Contacts
Phone
SMS
Calendar
Notifications
External/shared storage
Photos/media library
Bluetooth scanning
Nearby devices
Biometrics
Accessibility service
Internet
Network state
```

unless implementation later proves a concrete requirement and the decision is documented.

---

# 82. Permission Acceptance Criteria

Before family test:

- [ ] microphone is the only sensitive runtime permission;
- [ ] explanation appears before first request;
- [ ] parent can choose “Not now”;
- [ ] denial does not block child mode;
- [ ] denial does not cause repeated prompts;
- [ ] permanent denial offers settings only after adult intent;
- [ ] revoked permission is detected safely;
- [ ] child attempt PCM is never saved;
- [ ] family recording requires explicit parent action;
- [ ] merged manifest contains no unexplained dangerous permissions;
- [ ] airplane-mode operation is unchanged;
- [ ] no storage/location/camera/notification permission is requested.

---

# 83. Permission Invariants

### PERM-INV-001
Microphone is the only sensitive runtime permission in Prototype 0.1.

### PERM-INV-002
Microphone permission is optional for core learning.

### PERM-INV-003
Only adult-facing flows may initiate the microphone permission request.

### PERM-INV-004
Miozira explains microphone behavior before the system dialog.

### PERM-INV-005
Permission denial never dead-ends the child session.

### PERM-INV-006
The app does not nag after denial.

### PERM-INV-007
Parent setting OFF prevents microphone use even if system permission is granted.

### PERM-INV-008
System permission is rechecked rather than inferred from saved preference.

### PERM-INV-009
No broad storage permission is required.

### PERM-INV-010
No camera or location permission is required.

### PERM-INV-011
No notification permission is required.

### PERM-INV-012
No network permission is required by product design.

### PERM-INV-013
Final merged manifest is reviewed for unexpected permissions.

### PERM-INV-014
Permission copy must match actual privacy behavior.

---

# 84. Open Permission Decisions

Before release freeze, confirm:

1. exact parent onboarding copy;
2. whether microphone setting defaults OFF until parent enables, or ON only after explicit consent;
3. exact Android `DENIED_DONT_ASK_AGAIN` detection strategy per supported APIs;
4. whether `INTERNET` is completely absent from the merged release manifest;
5. whether any chosen audio library adds permissions;
6. exact iOS microphone permission mapping for future port.

---

# 85. Relationship to `microphone.md`

`microphone.md` defines what happens after permission is granted.

This document defines who may request that permission and what happens when it is absent.

---

# 86. Relationship to `parent-experience.md`

Parent UX owns:

- explanation;
- enable/disable setting;
- denial messaging;
- family recording permission entry.

---

# 87. Relationship to `platform-support.md`

`platform-support.md` defines microphone as optional hardware for core progression.

This document ensures the Android permission model matches that rule.

---

# 88. Relationship to `privacy.md`

`privacy.md` must disclose:

- optional microphone use;
- transient child attempt processing;
- local family recordings;
- no raw child-audio upload.

---

# 89. Relationship to `security.md`

`security.md` should verify:

- least privilege;
- merged manifest review;
- dependency permission audit;
- no broad storage/network access.

---

# 90. Decision Summary

Prototype 0.1 permission posture is intentionally minimal:

```text
Required sensitive runtime permission:
    RECORD_AUDIO

Not required:
    camera
    location
    contacts
    phone/SMS
    calendar
    notifications
    external storage
    photos/media
    Bluetooth scanning
    biometrics
    internet
```

Microphone permission is:

- adult-controlled;
- explained before request;
- optional;
- non-blocking when denied;
- never used continuously;
- shared by two clearly separated features:
  - child attempt detection;
  - parent family recording.

The governing rule is:

> **If Miozira cannot clearly explain why it needs a permission, it should not request it.**
