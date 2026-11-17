# Miozira — UI/UX Design

**Status:** Approved by the parent, 2026-11-17
**Date:** 2026-11-17 (discussion held 2026-11-16 and 2026-11-17)
**Builds on:** `2026-11-16-five-languages-and-thinking-puzzles-design.md` (the product design; it wins where older docs differ)
**Supersedes in part:** `docs/02-design/*` — see section 12

Markers used below: **(parent)** means the parent decided it in the discussion.
**(my call)** means the designer chose it and the parent has not been asked;
correct any of these when reading. All timings are starting values to be tuned by
watching him on the tablet.

---

## 1. Intent

Give one 4-year-10-month-old boy a calm screen he can use alone for one short
session a day, on which he hears and freely says words in five languages and
does one thinking puzzle, and give his parents a protected area that tells them
in plain words what he did. The screens must teach without hooking him.

## 2. Goals, non-goals, anti-goals

**Goals**

- He can run a whole session alone, without reading and without help.
- Every child screen has one obvious thing to do and large targets.
- The same action always has the same kind of result.
- Parents read progress as sentences and get four words to use that day.
- The parent area is invisible to him and cannot be opened by accident or by
  poking around.

**Non-goals**

- Teaching reading. The written word is shown for familiarity only.
- Telling him which language he is in. **(parent)** He just hears the word and
  links it to the picture.
- An upright (portrait) layout. **(parent)**
- Judging how he says a word — now or later.

**Anti-goals** (these make it a failure even if it works)

- Streaks, scores, levels, stars, rewards, confetti, pleading characters,
  visible timers, progress bars.
- Anything shown, coloured or sounded as right or wrong.
- Pressure to speak: a prompt repeated, made louder, or followed by "try again".
- Anything earned or unlocked. Nothing in the app is a prize. **(parent, on the
  scene idea: "if it is going to be a rewarding system I'd drop it")**
- Any mention of missed days, time spent, or how long since the last session.

## 3. Constraints

- Xiaomi Pad, about 11 inches, **sideways (landscape) only; the screen does not
  turn.** **(parent)** It also helps him sit in one place.
- Fully offline. No reminders or notifications of any kind.
- Low-stimulation palette from `docs/02-design/color-palette.md`; child
  background `#F2EEE7`. The picture is the most colourful thing on the screen.
- The only sounds are voices. No music, no instrument sounds, no effect sounds.
  **(parent, halal)**
- Pictures follow the halal rules in section 8. **(parent)**
- His microphone audio is never saved; the app only notices that he made a
  sound.
- Session size is about 8–10 minutes, but **a session is a planned set of
  words, and finishing the set matters, not the minutes.** **(parent)**

## 4. Approaches considered

| Topic | Options | Chosen | Why |
|---|---|---|---|
| "Your turn" in a parent's voice | Every word / only the first few words | Every word **(parent)** | To him it is company, not pushing. |
| Announcing the language of a block | Spoken cue / colour or shape cue / nothing | Nothing **(parent)** | He simply hears the word; the script under the picture is the only difference. |
| How the written word lights up | Whole word at once / letter-by-letter sweep | Whole word | He is not meant to read it; a sweep pulls the eye off the picture; whole-word behaves the same in all four scripts. |
| After a tap in "Which one?" | Name only what he tapped / name what he tapped, then the other | Name both **(parent)** | He always hears both words and the sequence is identical whichever he taps. |
| A scene with all the things together | Unlocked when a set is complete / always the ending, filling in quietly / not at all | Always the ending, filling in quietly **(parent)** | Keeps "connecting the dots" and removes the prize. |
| Opening the app after the session is done | Quiet screen / a new session each time | Quiet screen; extra session from the parent area **(parent)** | |
| Picture style | Painted realistic / cut-out photos / flat drawings | Painted realistic **(parent)** | Real things are recognisable at home; the scene and puzzle animations can match. |
| Living beings in pictures | With faces / without faces / not drawn | Without faces; body parts shown one at a time **(parent)** | |
| Orientation | Sideways only / both | Sideways only **(parent)** | |
| Guide-phrase recordings | One recording per phrase / both parents record every phrase | Both parents; voices alternate **(parent)** | |
| Session length | 8–10 min / 10–15 min | 8–10 min; the set matters, not the minutes **(parent)** | |

---

## 5. The child's screens

There are four kinds of child screen: the word screen, the two-picture screen,
the puzzle screens and the quiet screen. A fifth, the scene, comes later
(section 5.6). There is no home screen, menu, button bar or visible control on
any of them.

### 5.1 Rules for every child screen

- Plain `#F2EEE7` background. No frame, glow, shadow box or decoration.
- Everything he can tap is a picture, at least about 2.5 cm across, with clear
  space between targets. **(my call)**
- Nothing he can tap is near the top-right corner (the hidden parent corner).
- A tap is acknowledged at once by the picture growing very slightly.
- Taps while a word is being spoken are ignored, so voices never overlap.
- Motion is slow and single: a fade, a small grow-and-settle. Nothing bounces,
  spins, flashes or loops.
- No text other than the one written word (section 6).

### 5.2 The word screen

```
 1. Picture waits            2. He taps: word is spoken     3. His turn
┌───────────────────────┐   ┌───────────────────────┐      ┌───────────────────────┐
│                       │   │                       │      │       ╭ ─ ─ ─ ╮       │
│       [ APPLE ]       │   │       [ APPLE ]       │      │      ¦ [APPLE] ¦      │
│                       │   │                       │      │       ╰ ─ ─ ─ ╯       │
│                       │   │         apple         │      │         apple         │
└───────────────────────┘   └───────────────────────┘      └───────────────────────┘
```

Layout: the picture is centred and about 60% of the screen's height. The
written word sits centred beneath it. **The ring belongs to the picture and the
word is always below the ring with a clear gap; they never touch or overlap.**
**(parent)**

What happens, in order:

1. **Picture waits.** It fades in and is still. No written word yet. If he does
   nothing for about 8 seconds the picture gives one soft pulse, once.
2. **He taps anywhere on the picture.** The word is spoken in the block's
   language. The written word fades in lit (section 6) and settles.
3. **"Your turn"** is spoken in Malayalam in a parent's voice, once, after
   every word. **(parent)** Always the same short warm phrase. Never repeated,
   never louder, never followed by another prompt.
4. **His turn.** One calm ring (`#607C8A`) breathes slowly around the picture
   for about 4 seconds.
   - If the app hears him make a sound, the picture gives one small warm nod (a
     slight grow-and-settle). No sound, no colour. The nod is the same whatever
     he said: the app never judges how he says a word. **(parent: ignore how
     well he repeats; designer: this holds permanently, because the app can only
     tell that he made a sound)**
   - If he is silent, the ring fades. Nothing else happens.
5. **Moving on.** About a second later the picture cross-fades to the next one.
   There is no next button.

Other cases:

- **Tapping again** before the app moves on repeats the word and gives him one
  more turn.
- **Microphone off or not allowed:** everything looks the same, including the
  ring as a quiet space; only the nod never happens. **(my call)**
- **A word's sound fails to play:** the app moves quietly to the next picture.
  He never sees an error.

### 5.3 "Say it first" (familiar words only)

Same screen, with the turn moved to the front:

1. The picture appears **with no written word**. **(my call — the parent was
   asked and did not object)**
2. The ring breathes for about 3 seconds. Nothing is said; no "your turn".
3. The app says the word whether or not he spoke; the written word appears lit.
   If he spoke first, the picture nods before the word is said.
4. A short pause, then the next picture. Tapping during the ring makes the app
   say the word straight away.

### 5.4 "Which one?" (two pictures)

```
┌──────────────────────────────┐
│                              │
│   [ APPLE ]      [ BALL ]    │     the app says: "ball"
│                              │
└──────────────────────────────┘
```

- Two pictures side by side, equal size, no written words at first.
- The app says one of the two words. No tap is needed to hear it.
- **Whatever he taps, the same two things happen** **(parent)**:
  1. the picture he touched steps forward (grows a little), is named, and its
     written word appears beneath it;
  2. it settles, then the other picture steps forward, is named, and its word
     appears, in exactly the same way.
- If he taps nothing for about 8 seconds, the picture the app named steps
  forward first, then the other.
- Then the next item.

### 5.5 The thinking puzzles

One puzzle per session, using that day's words. The question is asked once in
Malayalam in a parent's voice.

**The one rule for every puzzle and for "Which one?":** after any tap,
(1) what he touched is named, then (2) the real answer plays out calmly. No
tick, cross, colour, sound effect, shake or "try again". If he taps nothing,
step 2 happens after a short wait (about 8 seconds). He learns by watching what
happens.

| Puzzle | What he sees | After any tap or the wait |
|---|---|---|
| **What's gone?** | Three pictures in a row, each named once. They fade out for about 2 seconds. Two return; one place is empty. | The missing picture fades back into its place and is named. He may say it, tap the empty place, tap anything, or wait: the result is the same. |
| **What comes next?** | A row: apple, ball, apple, ball, then an empty place marked by a faint dashed outline. Two choices below. | The touched choice is named. The right picture fades into the empty place by itself. Then the row is spoken across, each picture stepping forward in turn. |
| **Which is not the cat?** | Two pictures side by side. | The touched one is named; then the one that is not the cat steps forward and is named. |
| **What happens if?** *(later)* | A cup at the edge of a table. Two small pictures below: cup still standing, cup spilled. | The cup tips and spills, slowly, once. Then the word. |
| **Whose is this?** *(later)* | A close-up of an ear or a wheel. Two or three whole pictures below. | The touched one is named; then the close-up slowly widens to show the whole thing, which is named. |

Details:

- Small pictures in a row are still large targets only where they can be
  tapped; the row in "What comes next?" is for looking, the two choices are for
  tapping.
- Difficulty changes quietly (more pictures, longer rows). Nothing on screen
  shows a level.
- **Movement.** When a fitting word is in the day's set, the puzzle part opens
  with one movement prompt in a parent's voice beside the picture ("touch your
  nose"). The app waits about 5 seconds and moves on. Nothing is checked.
  **(my call)**

### 5.6 The scene (later step)

One calm painted room or place containing things he already knows.

```
┌──────────────────────────────────────────┐
│   (apple) (cup) (glass of water)         │
│  ═══════ table ═══════     (cat, from    │
│   ║              ║          behind)      │
│        (ball)                   (car)    │
│                 word appears here        │
└──────────────────────────────────────────┘
```

- It is the easy ending of every session, about a minute, then the app goes
  quiet. It is **never unlocked, earned or announced**. **(parent)**
- Only things he has already met are in it. A newly met thing is simply there
  next time. There are no empty outlines showing what is missing.
- He taps what he likes. The tapped thing comes forward by itself while its word
  is spoken, the written word appears beneath the scene, and the thing steps
  back. **No pinch-zoom.**
- At most six to eight things, each a large target.
- Words are spoken in the language of the block just finished. **(my call)**
- A parent's voice says "tap anything you like" once when it appears.
- It ends by itself after about a minute or after about ten taps, whichever
  comes first, finishing the word being spoken. **(my call)**
- Needs new artwork in the agreed style, so it is built after the first three
  puzzles. Until then the ending is one familiar word on the ordinary word
  screen. The starter words make one room (apple, cup, water, ball, cat, car);
  hand and nose sit out.

---

## 6. The written word

- Shown under the picture, centred, large (about a tenth of the screen's
  height), in the language's own script: Latin for English and Spanish, Tamil,
  Arabic, and Japanese.
- **Japanese is written the way a Japanese children's book writes each word**
  **(parent: "whichever is the most useful")**: everyday Japanese words in
  hiragana (りんご, ねこ, みず) and borrowed words in katakana (コップ, ボール).
  This refines "hiragana" in the product design. The checker confirms each
  spelling.
- **Arabic is written with its vowel marks.** **(parent)** The checker supplies
  the marked spelling, and the word area leaves enough height for the marks
  above and below the letters to stay clear.
- Arabic is laid out right to left. Because the word is a single centred item,
  nothing else on the screen moves or mirrors.
- **Lighting up:** the whole word fades in soft teal (`#4F746B`) as the word is
  spoken, then settles to dark (`#26312E`) and stays until the picture changes.
  No letter-by-letter sweep, no underline, no bounce.
- It never needs to be read and is never a tap target.
- The same lettering style is used for all four scripts and is carried inside
  the app, so the words look the same on any tablet. **(my call: the Noto
  family, trimmed to the letters actually used)**

## 7. A session from start to finish

```
open → warm-up → 1st language → breath → puzzle → breath → 2nd language → ending → quiet
```

- **Start.** Opening the app shows the first picture, still and waiting. His
  first tap begins the session. No home screen, logo animation or start button.
- **Warm-up.** Two or three words he knows, on the ordinary word screen, in the
  day's first language. **(my call)**
- **Language blocks.** Word screens, with "say it first" for familiar words and
  one or two "Which one?" items. Nothing announces the language. **(parent)**
- **Breath.** One second of plain background before and after the puzzle. This
  is the only transition marker in the session.
- **Ending.** One familiar favourite (later, the scene). Then a parent's voice
  says one closing phrase, such as "all done, off you go" — never "see you
  tomorrow". The picture fades slowly into the plain background.
- **Quiet screen.** Plain `#F2EEE7`, nothing on it. Taps do nothing and make no
  sound. After about 30 seconds the tablet is allowed to dim and sleep.
- **No clock.** Each part is a set number of items. The session ends when the
  set is done. Nothing counts down and no word is ever cut off.
- **He walks away.** After about a minute with no touch the app goes to the
  quiet screen. If he returns the same day, a tap carries on where he left off.
  Nothing is said about it, then or later.
- **App opened again after the session is finished.** The quiet screen.
  **(parent)** A new session is offered on the next day, or when a parent
  starts an extra one from the parent area.
- **Which languages.** Two per session, advancing per completed session, as in
  the product design. An unfinished session does not advance the rotation.

## 8. Pictures

**Style (parent):** realistic and softly painted, like the current apple and
ball drafts. One thing per picture, cut out, sitting directly on the warm
background. No glow, no frame, no room or surface behind it.

**Halal rules (parent):**

- Things, food, plants and vehicles: painted realistic.
- Animals: never showing a face. Use natural poses that hide it (seen from
  behind, curled asleep) in preference to a blank face. **(my call on the
  poses)**
- People: no faces. From behind, at a distance, or hands only; modestly
  dressed.
- Body parts: each shown by itself (a nose, a hand, an ear), never as part of a
  whole face or body.
- Eating and drinking use the right hand. No food or drink that is not halal.
- The scene and "Whose is this?" follow the same rules.

**The eight drafts:**

| Picture | Now | Action |
|---|---|---|
| Apple, ball | Painted, cut out | Keep; this is the reference look. |
| Car | Painted, with a red glow | Redo without the glow. |
| Nose | A nose by itself, with a glow | Right idea; redo without the glow. |
| Hand | Photo of an adult's hand with a ring, on grey | Replace with a painted child's hand, cut out. |
| Cup | Shop-style photo on a white box; source unknown | Replace with a painted cup. |
| Cat | Photo with a face, in a room | Replace with a painted cat seen from behind or curled asleep. |
| Water | Dramatic splash close-up on dark blue | Replace with a painted glass of water. |

**Several pictures per word:**

- Three per word to begin with (a red apple, a green apple, a cut apple).
- A new word always uses the same first picture. The others join only once the
  word is familiar.
- They differ in the thing shown, never in the style.

## 9. The guiding voice

All instructions are in Malayalam. **Both parents record every phrase and the
app alternates their voices.** **(parent)** One parent's voice is used for a
whole session and the other for the next, so a session sounds like one person;
if a phrase is missing in that voice the other parent's is used, and a prepared
voice only if neither exists. **(my call)**

Proposed phrases (English meaning; the parents say them in their own
Malayalam):

| # | Phrase | Used |
|---|---|---|
| 1 | Let's begin. | After his first tap of a session (optional). |
| 2 | Your turn. | After every word. |
| 3 | Show me… | "Which one?", with the word in the learning language. |
| 4 | What's gone? | Puzzle. |
| 5 | What comes next? | Puzzle. |
| 6 | Which one is not… | Puzzle, with the word in the learning language. |
| 7 | What will happen? | Puzzle (later). |
| 8 | Whose is this? | Puzzle (later). |
| 9 | Tap anything you like. | The scene (later). |
| 10 | Touch your nose. | Movement. |
| 11 | Show me your hand. | Movement. |
| 12 | Stand up and have a big stretch. | Movement. |
| 13 | All done, off you go. | Closing. |

Thirteen phrases, two voices, 26 short clips; room for two more.

Phrases 3 and 6 wrap a word from the learning language. In Malayalam the word
may naturally come before the phrase, so each of these is recorded as one piece
and the app can play it before or after the word. **(my call)**

## 10. The parent area

### 10.1 Getting in

```
 Step 1: hidden corner               Step 2: a reading step
┌───────────────────────────┐       ┌───────────────────────────┐
│                      ·····│       │  Parents only             │
│       [ APPLE ]      hold │       │  Type this number:        │
│                      3 sec│       │  forty-seven              │
│         apple             │       │  [1][2][3][4][5]          │
└───────────────────────────┘       │  [6][7][8][9][0]          │
                                    └───────────────────────────┘
```

- Nothing is drawn in the corner: no icon, gear or link on any child screen.
  A short tap there does nothing. Holding shows no progress ring.
- After a 3-second hold in the top-right corner, a plain adult screen asks for a
  two-digit number written as a word. The number changes every time.
- A wrong answer, or 10 seconds of nothing, returns silently to exactly where he
  was, with no sound or animation, so it is not worth poking at.
- The corner works from every child screen, including the quiet screen.
- The parent area locks again when a parent leaves it or the screen turns off.
- Later, when he can read number words, this step is replaced by a PIN the
  parents choose. Not built now.

### 10.2 Parent home

Calm adult screen on `#F6F3EE`, ordinary text and buttons, in English.

```
┌──────────────────────────────────────────────┐
│ Miozira for parents            [Lock and go back] │
├──────────────────────────────────────────────┤
│ Four words for today                          │
│  • At breakfast, when you pour: water (Tamil) │
│  • At snack time, handing a slice: apple (Tamil) │
│  • At bath time: nose (English)               │
│  • At dinner, passing it: cup (English)       │
├──────────────────────────────────────────────┤
│ Most recent session: English and Tamil        │
│  Tried saying: apple, ball, cup, water (Tamil)│
│  Said before the app did: ball, cup (Tamil)   │
│  Listened without speaking: nose, cat         │
│  Puzzle, What's gone?: he chose before the    │
│  answer came back.                            │
├──────────────────────────────────────────────┤
│ Noticed at home                               │
│  Cup, in Tamil   [He understood it] [He said it] │
├──────────────────────────────────────────────┤
│ How each language is going                  › │
│ How the puzzles are going                   › │
│ Voices                                      › │
│ Start an extra session                      › │
│ Microphone and privacy                      › │
└──────────────────────────────────────────────┘
```

- **Four words for today (parent):** four words from the most recent session,
  each with the moment of the day to say it. Replaces the single suggestion in
  today's build. No tick boxes. It is seen only when a parent opens the area;
  the app sends no reminders.
- **Most recent session:** sentences only. The categories are "tried saying",
  "said before the app did", "listened without speaking", and one line about
  the puzzle. Never "missed", "failed", "wrong", "learned" or "mastered".
- **No numbers, dates or durations anywhere.** The "Heard 3 times" line in
  today's build is removed. Sessions are "most recent" and "before that".
- **Noticed at home:** the two existing buttons, reworded "He understood it"
  and "He said it". One tap saves; the screen answers "Saved."
- First use, before any session: "Miozira will describe his sessions here."

### 10.3 The other parent screens

- **How each language is going.** Five short paragraphs, one per language, for
  example: "Tamil — he tries most of his Tamil words. Said before the app did:
  cup, water. Still just listening: nose." Words such as "most", "some" and
  "not yet" stand in for counts.
- **How the puzzles are going.** One sentence per puzzle kind he has met, for
  example: "What's gone? — usually joins in." "What comes next? — still
  watching." "Not met yet" for the rest.
- **Voices.** Two lists.
  - *Guide phrases in Malayalam:* one row per phrase with two slots, Mother and
    Father. Each slot shows "Recorded" with Play and Record again, or "Not
    recorded yet" with Record. A line at the top: "Until you record a phrase, a
    prepared voice is used. Recordings stay on this tablet."
  - *Tamil words:* the existing family-voice screen, one row per word, same
    controls.
- **Start an extra session.** One confirmation ("Start another session now?"),
  then the parent area locks and the child's first picture is waiting.
- **Microphone and privacy.** The existing explanation of what the microphone
  is used for, the on/off switch, and "Erase everything on this tablet", which
  lists what will be erased and asks for a second confirmation.

First-time setup (explanation and microphone permission) is unchanged from
today's build.

## 11. Order of work

This fits the order in the product design; nothing here needs a new step.

| Product step | What this document adds |
|---|---|
| 2. Five languages, session shape, written words | Sideways lock; word screen with written word and ring geometry; session flow, breath, closing and quiet screen; stronger parent gate; parent home in words with four words for today; picture style and the halal rules for all new and replaced pictures. |
| 3. Say-it-first, Which one?, varied voices | Sections 5.3 and 5.4; three pictures per word. |
| 4. Malayalam guiding voice and first three puzzles | Section 9 and the Voices screen with two parent slots; the first three puzzle screens; movement prompt. |
| 5. Puzzles needing new artwork | "What happens if?", "Whose is this?", and the scene. |

## 12. Conflicts with the older design docs, and what to update

**Applied 2026-11-17** at the parent's request: every change below has been made
in `docs/02-design/`. Each file has a "Revision — 2026-11-17" note at the top;
contradicting sections were rewritten in place, and new screens are referenced
back to this document so there is one source of truth.

**All seven files:** the header says "8 concepts × English + Tamil" and, where
present, "Portrait + Landscape". Change to five languages and landscape only.

| File | What conflicts | Change to make |
|---|---|---|
| `ui-ux.md` | §7, §8, §9 and UX-005 describe portrait and landscape. | Landscape only; remove the portrait layout and the rotation rules. |
| | UX-P03, §13, §52 and UX-INV-001: little or no text for the child. | One written word under the picture, shown but never required (section 6). |
| | §16: minimise spoken prompts; §84 item 5 asks whether "Your turn" is needed. | "Your turn" in a parent's Malayalam voice after every word. |
| | §17 and §63: the ring is a microphone indicator and is hidden when the mic is off. | The ring is "his turn" and always shows; only the nod depends on the mic. |
| | §25 language switching options A–C. | Settled: no cue. Remove B and C. |
| | §27 movement, §28–29 session end and rest. | Replace with sections 5.5 and 7 here (closing phrase, quiet screen, same-day rule). |
| | §30–32 parent gate: about 2 seconds, optional confirmation. | 3-second hidden corner plus the number-word step (section 10.1). |
| | §33–40, §81: one suggestion, "Recent learning", "Recognized/Used outside Miozira". | Four words for today; session described in sentences; buttons reworded. |
| | §41–42, §82 family recordings per concept and language. | Voices screen: guide phrases with Mother and Father slots, plus Tamil words. |
| | §46–47 language settings (enable languages, starting language). | Remove; the rotation decides. |
| | §53 meaningful motion (car rolls, water pours); §55 soft non-word sounds. | No sounds other than voices. Motion limited to what section 5 describes and the two later puzzle animations. |
| | §73 child screen count of two; §84 open questions 1, 3, 4, 5, 7. | Four screen kinds plus the scene; mark those questions answered. |
| | Add | "Which one?", the five puzzles, the one rule after any tap, "say it first", the scene. |
| `child-experience.md` | §26–28 language switching; §36 orientation change. | No cue, no judgement of preference (keep §28); delete orientation. |
| | §8, §38 speaking invitation and microphone cue. | "Your turn" every word; ring as his turn. |
| | §29–30 movement; §31–33 inactivity and stopping. | Align with sections 5.5 and 7 here. |
| | §44, §64 child visual language; §63 child-facing language. | Add the written word, the picture style, the halal rules; guide voice is Malayalam, both parents. |
| | Add | The puzzle experience and the "nothing is earned" principle (scene). |
| `design-system.md` | Portrait margins and §56–57 portrait/landscape behaviour; checklist items for portrait. | Landscape only. |
| | §13 child typography (little or none); §72 Tamil typography only. | Child word style for four scripts incl. right-to-left Arabic; carried lettering. |
| | §38 RealWorldSuggestionCard; family recording item; §46 gate visual. | Four-words card; voice row with two slots; number-word gate screen. |
| | Add | The ring, the "step forward" motion, the empty-place outline, the quiet screen, child target size, picture style and halal rules. |
| `interaction-states.md` | `OrientationChanged` events and §20 throughout. | Remove. |
| | Child states stop at the single word loop. | Add say-it-first, Which one?, each puzzle, breath, closing, quiet screen, same-day resume, session-finished-today. |
| | §31–36 gate states. | Add the number-word step, the 10-second timeout and silent return. |
| | Session end generates one suggestion. | Generates four words for today. |
| `parent-experience.md` | §7–8 language setup and starting language. | Remove; five languages, rotation decides. |
| | §14–16 gate. | Section 10.1 here. |
| | §19–22 single suggestion. | Four words for today. |
| | Around line 439, "Heard 3 times" and descriptive counts. | No counts; sentences only. |
| | §33–39 family recordings. | Voices screen: guide phrases (two parents) and Tamil words. |
| | Add | Start an extra session; how each language is going; how the puzzles are going. |
| `accessibility.md` | ACC-LAYOUT-001/002, §23 and checklist: portrait and landscape. | Landscape only. |
| | §26: child-visible words are supplementary "unless a later product revision changes the pre-reader design". | Still supplementary; record that the word is now always shown. |
| | §25 Tamil script rendering only. | Add Arabic (right-to-left) and Japanese. |
| | ACC-GATE-001/002: alternate adult path, not long-press only. | Decide whether the number-word step alone is enough; note the corner works from the quiet screen. |
| `color-palette.md` | No conflict. | Add the uses: ring `#607C8A`, lit word `#4F746B`, settled word `#26312E`. |

Outside `docs/02-design/`: the project guides (`CLAUDE.md`, `AGENTS.md`) and the
scope document still describe English + Tamil and both orientations; they are
already due for revision under the product design's section 9.

## 13. Notes for the learning plan (not decided here)

The parent proposed selection rules on 2026-11-17. The screen-related parts are
in section 10. The rest belongs to the learning-engine design, with these
adjustments agreed in discussion:

- Never more than three new words; defer a word that is not landing and offer
  an easier one in the same language.
- Words he stayed quiet on come back inside the language block, after the
  warm-up, not first.
- Count that language's visits, not days: "not heard in its last couple of
  visits", "quiet on three visits in a row". The app must not count days.
- Classify conservatively and only from what can be observed: spoke before the
  app did; tapped the named picture in "Which one?"; made a sound after the
  word; no attempt.

## 14. Left to the builder

- All timings (the turn length, the waits, the fades, idle time) and exact
  sizes, tuned by watching him.
- How many items make each part of the set.
- The wording of the parent-area sentences beyond the examples here, keeping to
  the allowed words in section 10.2.
- How the four daily moments are chosen for each word.
- The lettering files and how they are trimmed.

## 15. Open items for the parent

- **Feelings words and family words are not pictured in the app.** **(parent,
  2026-11-17)** They cannot be shown without faces, so the parents will learn
  and teach them at home. `docs/01-learning/word-bank.md` categories 8 (Family
  and people) and 16 (Feelings) are marked "taught at home, not in the app"
  (done 2026-11-17).
- **"Drum" is replaced by another toy.** **(parent, 2026-11-17)** A duff has no
  everyday word in Japanese, Spanish or Tamil, and the word bank only takes
  words common to all five languages. The word bank now has "bucket" in its
  place (designer's pick, done 2026-11-17; swap it for any toy with an everyday
  word in all five languages).
- Record the 26 guide-phrase clips (13 phrases, both parents).
- Replace or repaint six of the eight pictures (section 8).

## 16. Mockups

Interactive mockups were shown in the discussion (word screen in five
languages, the ring fix, the scene, "Which one?", "What comes next?", parent
home and voices). The text sketches in this document are the record.
