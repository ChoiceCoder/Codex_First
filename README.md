# GovtPrep

Production-ready Android mock test app (Kotlin + Compose + MVVM + Hilt + Supabase).

## Setup
1. Create Supabase project.
2. Run SQL from `docs/supabase_schema.sql`.
3. Enable Email auth in Supabase Auth settings.
4. Put `SUPABASE_URL` and `SUPABASE_ANON_KEY` in `app/build.gradle.kts` buildConfigField values.
5. Build and run on Android API 24+.

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
