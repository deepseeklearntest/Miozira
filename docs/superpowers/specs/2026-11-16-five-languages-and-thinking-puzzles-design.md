# Miozira — Five Languages, Session Shape and Thinking Puzzles

**Status:** Draft for approval
**Date:** 2026-11-16
**Revises:** `docs/00-foundation/prototype-0.1-scope.md` (see "Scope rules this changes")

---

## 1. Intent

Miozira is first for one child: a boy of 4 years 10 months, fluent in Malayalam,
who understands everyday English (with an accent picked up from Bluey) and a
little Tamil. His parents speak Malayalam and Tamil. His screen time is tightly
limited, so every minute must be productive and must not hook him.

The parents want him to build spoken vocabulary in five languages — English,
Tamil, Japanese, Arabic and Spanish — with equal standing, and to practise
memory, reasoning and logic along the way. Distribution and revenue are "nice if
it happens", after it works for him.

## 2. Goals

- All five languages are present from the first week and get equal time.
- He understands the words when he hears them and can say them from memory on
  seeing the object.
- One session a day of about 8–10 minutes, usable by him alone.
- One short thinking puzzle per session, built from the day's words.
- Parents can see that he is willingly trying to speak, without listening to
  recordings.

## 3. Non-goals

- Recording or storing his voice. Detecting that he tried is enough.
- Judging or scoring pronunciation.
- Teaching reading. Written words are shown for familiarity only.
- A promise of higher general intelligence. Puzzles are practice of specific
  skills.
- Hindi (he will pick it up from his surroundings) and Malayalam as a learning
  language (he is already fluent).
- Building more than the existing 8 concepts in this round. The full menu of
  about 150 concepts is kept in `docs/01-learning/word-bank.md` and is added one
  category at a time afterwards.

## 4. Anti-goals

These would make it a failure even if it works:

- Anything that makes him want the tablet for the tablet's sake: streaks,
  scores, levels, rewards, celebration effects, characters that plead.
- The app noticing or commenting on missed days. Consistency is a family
  routine, not an app rule.
- Showing him that an answer was wrong.
- Pressure to speak. He imitates freely and refuses when pushed.

## 5. Constraints

- Fully offline; no network permission; nothing leaves the tablet.
- His microphone audio stays in memory and is never saved.
- Runs on the family's Xiaomi Pad (Android).
- Parents cannot promise to sit with him, so the app must carry a session alone.
- Nobody in the family speaks Japanese, Spanish or Arabic.

## 6. Approaches considered

How five languages fit into a few minutes a day:

| Approach | Trade-off |
|---|---|
| **A. Two languages a day, in separate blocks (chosen)** | Languages stay distinct; each returns every two or three sessions; less time per language per day. |
| B. All five on every word | Words blur into a chant; he learns a sequence, not five languages. |
| C. One language a day | Cleanest separation, but about a week between visits is too long at his age. |

The parent chose A: less time per language, but better odds the words stick.

## 7. The design

### 7.1 One session

About 8–10 minutes, once a day, ideally in the morning. The app ends the session
itself and goes quiet; there is no visible timer.

| Part | Length | What happens |
|---|---|---|
| Warm-up | ~1 min | Two or three words he already knows. |
| First language | ~3 min | Mostly familiar words; at most two or three new ones. |
| Thinking puzzle | ~2 min | One puzzle using the day's words; includes getting him to move where it fits. |
| Second language | ~3 min | Same pattern in the day's other language. |
| Easy ending | ~1 min | A familiar favourite, then the app goes quiet. |

He may stop at any moment with no consequence.

### 7.2 Which languages on which day

- Each session has two of the five languages.
- The rotation advances per completed session, not per calendar day, so a
  skipped day never skips a language and the app has no idea of a "missed" day.
- Pairings rotate so the same two languages are not always together.
- Over any five sessions each language appears twice.

### 7.3 How a word is taught

- **Tap, hear, invited to say** — the existing loop, unchanged.
- **Spaced return** — words come back after growing gaps (existing adaptive
  engine).
- **Say it first** — once a word is familiar, the picture appears and the app
  waits briefly before speaking, giving him the chance to say it from memory.
- **Varied voices** — the same word in more than one voice or pace (slower,
  normal, a second speaker). This replaces any idea of pronunciation scoring.
- **Varied pictures** — more than one picture per concept over time.
- **Which one?** — two pictures; the app says a word; whichever he taps, the app
  calmly names what he tapped. Nothing is marked wrong.

### 7.4 Written words

The word appears under the picture in that language's own script (Latin, Tamil,
Arabic, Japanese hiragana), fading in and gently highlighting as it is spoken.
It is never needed to use the app. Expected benefit: awareness that each
language looks different, and familiarity with some letter shapes.

### 7.5 The guiding voice

All instructions to him ("your turn", "what's gone?") are spoken in Malayalam,
recorded by his parents — about fifteen short phrases. This keeps the five
learning languages separate from the instruction language and lets him use the
app alone while still hearing a parent. A prepared voice is the fallback until
the recordings exist.

### 7.6 Voices for the words

| Language | Voice |
|---|---|
| English | Prepared voice with an Australian accent. |
| Tamil | Conversational forms (`கப்`, `தண்ணி`), recorded by the parents. |
| Arabic | Forms shared by formal and spoken Arabic; checked by the family's Arabic contact. Quranic words are a later, separate set. |
| Japanese, Spanish | Prepared voice, checked (ideally re-recorded) by a native speaker in one short paid tutor session per language. |

No Arabic, Japanese or Spanish word reaches him before a native speaker has
checked it.

**Audio quality is the first blocker (raised by the parent 2026-11-16).** The
current draft clips sound robotic and words such as "ball" sound cut off.
Measured on the bundled drafts: only 0–40 ms of silence before the word, and the
word itself spoken in about 0.4–0.5 s, which is rushed. Every clip must
therefore:

- be spoken slowly and warmly, the way an adult speaks to a small child;
- have a clear pause before and after the word (about 150 ms before and 250 ms
  after, tuned by ear on the tablet);
- be listened to and accepted by a parent before it is bundled.

**How the voices are produced (parent decision 2026-11-16).** This supersedes
the voice column in the table above. Tamil is recorded by the parents. Every
other language uses a computer voice, tried in this order until a parent accepts
the result by ear:

1. Kokoro (free, runs on the development computer) — English, Japanese, Spanish.
2. Gemini TTS (pay as you go) — also the first choice for Arabic, which Kokoro
   does not cover.
3. The best paid service available (ElevenLabs, Fish Audio or similar).

Each clip may say the word twice ("Ball. … Ball!") rather than once, so the
voice has context to shape its tone; this form needs no article, so it is
correct for "apple", "water" and every language. A parent chooses between the
single and repeated form by ear. Clips are generated on the development
computer and bundled; the app itself stays offline, and only the words — never
anything about the child — are sent to a voice service. The native-speaker check
for Arabic, Japanese and Spanish still applies to the generated clips.

### 7.7 Thinking puzzles

One per session, rotating in kind, always using that day's words.

| Puzzle | What he does | Skill |
|---|---|---|
| What's gone? | Three pictures show, hide, return with one missing; he says or taps it. | Memory |
| What comes next? | Apple, ball, apple, ball, … he picks the next. | Patterns, logic |
| Which is not the cat? | He must pick the other one. | Self-control, flexibility |
| What happens if? | A cup tips: stay or spill? He chooses, then watches. | Cause and effect |
| Whose is this? | He sees only an ear or a wheel and works out the whole. | Inference |

Rules:

- No wrong answer is ever shown; the app plays out the real result calmly.
- Difficulty adjusts quietly; no levels or scores are visible to him.
- The first three kinds need no new artwork and come first. The last two need
  new pictures and short animations and come after.

### 7.8 What parents see

In words, not numbers: which words he tried to say, which he said before the
app did, which puzzle kinds he manages and enjoys, and the existing after-session
suggestion for carrying a word into the day.

## 8. Order of work

Each step is its own plan.

0. **Voices he can understand.** Replace the 16 English and Tamil draft clips
   with clear, warm, unhurried ones. Nothing else is worth testing until a
   parent can follow every word.
1. **Put the English + Tamil build on the Xiaomi Pad and watch him use it.** No
   new building. What he does in the first minutes may change the rest.
2. Five languages, the two-a-day rotation, the new session shape and written
   words.
3. Say-it-first, Which one?, and varied voices.
4. Malayalam guiding voice and the first three puzzles.
5. The two puzzles that need new artwork.

## 9. Scope rules this changes

Decided by the parent on 2026-11-16; the scope document and
`docs/OPEN-QUESTIONS.md` are to be updated as part of step 2:

- Two languages becomes five.
- "No written vocabulary for the child" becomes "written words shown, never
  required".
- Thinking puzzles are added to the session.
- Tamil wording questions OQ-001 and OQ-002 resolve to the conversational forms.

Unchanged: offline, no saved child audio, no pronunciation scoring, no
gamification, no correct/wrong labels.

## 10. Left to the builder

- Exact timings, the wait length in "say it first", and the puzzle difficulty
  steps — all to be tuned by watching him.
- How the day's two languages are signalled to him at the start of each block.
- The list of Malayalam guide phrases.
- Screen layouts for the puzzles.

## 11. Open items for the parent

- Find and book the Japanese and Spanish tutor sessions.
- Confirm which spoken Arabic the family's contact speaks.
- Record the Tamil words and the Malayalam guide phrases.
