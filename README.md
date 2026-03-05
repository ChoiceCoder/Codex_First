# GovtPrep

Production-ready Android mock test app (Kotlin + Compose + MVVM + Hilt + Supabase).

## Setup
1. Create Supabase project.
2. Run SQL from `docs/supabase_schema.sql`.
3. Enable Email auth in Supabase Auth settings.
4. Add credentials in **any one** of these places:
   - global `~/.gradle/gradle.properties`
   - project `gradle.properties`
   - project `local.properties`
   using either standard keys:
   - `SUPABASE_URL=https://<your-project-ref>.supabase.co`
   - `SUPABASE_ANON_KEY=<your-anon-key>`
   or supported aliases:
   - `supabase.url=...`
   - `supabase.anon.key=...`
5. In Android Studio run: **Sync Project with Gradle Files** → **Build > Clean Project** → **Build > Rebuild Project**.
6. Uninstall old app from device/emulator and install again (old APK keeps stale BuildConfig values).

## Architecture
- MVVM with repositories.
- Hilt DI module in `di/AppModule.kt`.
- Supabase integration in `data/remote/SupabaseDataSource.kt`.
- Feature UI in Compose screens and `navigation/GovtPrepNavHost.kt`.

## Folder Structure
- `app/src/main/java/com/govtprep/data/model`
- `app/src/main/java/com/govtprep/data/remote`
- `app/src/main/java/com/govtprep/data/repository`
- `app/src/main/java/com/govtprep/di`
- `app/src/main/java/com/govtprep/ui/components`
- `app/src/main/java/com/govtprep/ui/screens`
- `app/src/main/java/com/govtprep/ui/navigation`
- `app/src/main/java/com/govtprep/ui/theme`
- `app/src/main/java/com/govtprep/viewmodel`
