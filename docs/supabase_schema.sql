create table if not exists users (
  id uuid primary key,
  email text not null unique,
  name text not null,
  streak int not null default 0,
  xp int not null default 0,
  is_admin boolean not null default false,
  created_at timestamptz not null default now()
);

create table if not exists subjects (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  description text not null,
  created_at timestamptz not null default now()
);

create table if not exists tests (
  id uuid primary key default gen_random_uuid(),
  subject_id uuid not null references subjects(id) on delete cascade,
  title text not null,
  duration_minutes int not null,
  total_marks int not null,
  created_at timestamptz not null default now()
);

create table if not exists questions (
  id uuid primary key default gen_random_uuid(),
  test_id uuid not null references tests(id) on delete cascade,
  prompt text not null,
  options jsonb not null,
  correct_index int not null,
  marks int not null default 1,
  created_at timestamptz not null default now()
);

create table if not exists attempts (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references users(id) on delete cascade,
  test_id uuid not null references tests(id) on delete cascade,
  score int not null,
  percentage numeric not null,
  percentile numeric not null,
  xp int not null,
  completed_at timestamptz not null,
  created_at timestamptz not null default now()
);

create table if not exists answers (
  id uuid primary key default gen_random_uuid(),
  attempt_id uuid not null references attempts(id) on delete cascade,
  question_id uuid not null references questions(id) on delete cascade,
  selected_index int not null,
  is_correct boolean not null,
  created_at timestamptz not null default now()
);
