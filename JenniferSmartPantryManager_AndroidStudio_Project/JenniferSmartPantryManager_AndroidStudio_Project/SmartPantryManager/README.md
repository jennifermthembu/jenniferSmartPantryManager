# Smart Pantry Manager

A native **Android** application written in **Java** that helps users cut food
waste by tracking the ingredients they already have at home and suggesting
recipes they can cook using **strictly** those leftover ingredients — no
shopping trip required.

> Module: Mobile App Development 700 · Practical Assignment (Portfolio of Evidence)

---

## What the app does

- **Pantry management** — add, edit and delete ingredients (name, quantity, unit
  and an optional expiry date) with input validation.
- **Pantry list** — a `RecyclerView` backed by Cloud Firestore showing every
  ingredient currently at home.
- **Recipe collection** — **20 recipes** pre-loaded (seeded) into the database on
  first run, each with required ingredients and preparation steps.
- **Suggested Recipes** — runs the **strict-matching rule** and lists *only* the
  recipes the user can make right now.
- **Recipe detail** — full ingredient list and method for a selected recipe.
- **Settings** — expiring-soon alert toggle, preferred-units choice, and an
  optional “Almost There” toggle (bonus feature).
- **Accounts** — create an account or log in with an email and password; sign
  out from Settings.
- **Friendly empty state** when nothing matches the current pantry.

### The strict-matching rule (core logic)

A recipe is only suggested when **every** ingredient it requires is present in
the pantry **in at least the required quantity**. If a recipe needs 5
ingredients and the pantry has 4 of them, it will **not** appear in the
suggestions list.

Matching is made robust to trivial real-world messiness without a full NLP
solution:

- case-insensitive and whitespace-tolerant (`" Tomato "` = `"tomato"`),
- simple singular/plural handling (`"tomato"` matches `"tomatoes"`,
  `"berry"`/`"berries"`, `"potato"`/`"potatoes"`),
- unit synonyms (`g`/`gram`/`grams`, `tbsp`/`tablespoon`, …),
- quantity is enforced only when the units are directly comparable.

The logic lives in
[`logic/RecipeMatcher.java`](app/src/main/java/com/richfield/smartpantry/logic/RecipeMatcher.java)
and [`logic/IngredientNormalizer.java`](app/src/main/java/com/richfield/smartpantry/logic/IngredientNormalizer.java),
and is covered by unit tests in
[`RecipeMatcherTest.java`](app/src/test/java/com/richfield/smartpantry/logic/RecipeMatcherTest.java).

**Bonus (optional):** a clearly separated “Almost There” list shows recipes
missing exactly one ingredient. It can be toggled on in Settings and is kept
completely separate from the strict suggestions.

---

## Database: Firebase Cloud Firestore

The app stores pantry items and recipes in Cloud Firestore through
[`db/DatabaseHelper.java`](app/src/main/java/com/richfield/smartpantry/db/DatabaseHelper.java).
Pantry items and recipes are shared across app installations. Recipes are
seeded on first use into the `recipes` collection; each recipe document embeds
its ingredient list. Pantry CRUD uses the `pantry_items` collection. Firebase
email/password authentication is required before the app can access Firestore.

### Firebase setup

1. Create a Firebase project, add an Android app with application ID
   `com.richfield.smartpantry`, and download `google-services.json` into the
   `app/` directory.
2. In Firebase Authentication, enable the **Email/Password** sign-in provider.
3. Create a Cloud Firestore database and publish the project rules in
   [`firestore.rules`](firestore.rules) for project
   `smart-pantry-manager-ae001`. In Firebase Console, open **Firestore Database
   → Rules**, replace the rules with the contents of `firestore.rules`, and
   select **Publish**. These rules allow authenticated app clients to read and
   write the shared pantry and recipe collections. Alternatively, use
   `firebase deploy --only firestore:rules --project smart-pantry-manager-ae001`.
   Saving an ingredient fails with `PERMISSION_DENIED` until these rules are
   published.
4. Open the project in Android Studio and sync Gradle.

The `google-services` Gradle plugin is activated when `app/google-services.json`
is present. Without the Firebase project configuration, the app cannot connect
to Firestore.

---

## Screens (6)

| # | Screen | Type | Notes |
|---|--------|------|-------|
| 1 | Sign in / Create account | Activity | Firebase email/password authentication |
| 2 | Pantry List | Fragment | RecyclerView + custom adapter, FAB to add |
| 3 | Add / Edit Ingredient | Activity | Launched via Intent, input validation |
| 4 | Suggested Recipes | Fragment | Runs the strict-matching rule |
| 5 | Recipe Detail | Activity | Receives recipe id via Intent extra |
| 6 | Settings | Fragment | AndroidX Preferences and sign out |

Navigation uses a **BottomNavigationView** (host `MainActivity`) for the three
primary Fragments, and explicit **Intents** to open the two child Activities
and pass data between them.

---

## Project structure

```
app/src/main/java/com/richfield/smartpantry/
  model/     PantryItem, Recipe, RecipeIngredient
  db/        DatabaseHelper (Cloud Firestore), SeedData
  logic/     RecipeMatcher, IngredientNormalizer   <- core strict-matching
  adapter/   PantryAdapter, RecipeAdapter           <- custom RecyclerView adapters
  ui/        AuthActivity, MainActivity, PantryListFragment, AddEditIngredientActivity,
             SuggestedRecipesFragment, RecipeDetailActivity, SettingsFragment
app/src/test/java/.../logic/RecipeMatcherTest.java  <- JUnit tests
```

---

## Setup & run

1. Install **Android Studio** (Hedgehog or newer) with an SDK for API 34.
2. **Open** this project folder in Android Studio and let Gradle sync.
3. Create an emulator (or connect a device) running **API 24+**.
4. Complete the Firebase setup above.
5. Press **Run** (▶). The recipe collection seeds automatically on first use.

### Try the strict-matching rule

1. Create an account or log in, then open **Suggestions** — with an empty pantry you'll see the empty-state message.
2. Go to **Pantry** and add, e.g. `Eggs 6 pcs`, `Butter 100 g`, `Salt 1 tsp`.
3. Back on **Suggestions**, *Scrambled Eggs* now appears.
4. Delete `Salt` — any recipe that needed salt disappears from the list.
5. Close and reopen the app — your pantry data persists in Firestore.

### Run the unit tests

```
./gradlew test
```

---

## Out of scope (by brief)

No Google Maps / mapping SDK / GPS or location features, no payments, and the
app is not published to the Play Store. Firestore requires Internet access;
the app requests no location permission.
