BEGIN TRANSACTION;

--  Activate extension to generate UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. function to update the updated_at column on row modification
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

-- ==========================================
-- TABLE: USER
-- ==========================================
CREATE table if not EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- store the hash, NEVER THE PLAIN PASSWORD !!!
    icon_path VARCHAR(255), -- path to user icon/image,
    role VARCHAR(50) DEFAULT 'USER', -- 'USER' or 'ADMIN'
    created_by VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(50),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Trigger pour user
DROP TRIGGER IF EXISTS update_users_modtime ON users;
CREATE TRIGGER update_users_modtime BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

-- ==========================================
-- TABLE: EXERCISE
-- ==========================================
CREATE table if not EXISTS exercises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    muscle_group VARCHAR(50) NOT NULL, -- Ex: 'Pectoraux', 'Dos'
    created_by VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(50),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

DROP TRIGGER IF EXISTS update_exercises_modtime ON exercises;
CREATE TRIGGER update_exercises_modtime BEFORE UPDATE ON exercises FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

-- ==========================================
-- TABLE: WORKOUT
-- ==========================================
CREATE table if not EXISTS workouts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    workout_date TIMESTAMPTZ DEFAULT NOW(),
    created_by VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(50),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

DROP TRIGGER IF EXISTS update_workouts_modtime ON workouts;
CREATE TRIGGER update_workouts_modtime BEFORE UPDATE ON workouts FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

-- ==========================================
-- TABLE: WORKOUT_EXERCISE
-- ==========================================
CREATE table if not EXISTS workout_exercises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id UUID NOT NULL REFERENCES exercises(id) ON DELETE RESTRICT,
    exercise_order INTEGER DEFAULT 0,
    notes TEXT,
    created_by VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(50),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

DROP TRIGGER IF EXISTS update_workout_exercises_modtime ON workout_exercises;
CREATE TRIGGER update_workout_exercises_modtime BEFORE UPDATE ON workout_exercises FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

-- Index to speed up queries filtering by workout_id
CREATE INDEX IF NOT EXISTS idx_workout_exercises_workout_id ON workout_exercises(workout_id);

-- ==========================================
-- TABLE: SET
-- ==========================================
CREATE table if not EXISTS sets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_exercise_id UUID NOT NULL REFERENCES workout_exercises(id) ON DELETE CASCADE,
    reps INTEGER NOT NULL,
    weight NUMERIC(5,2) NOT NULL,
    rpe NUMERIC(3,1),
    set_order INTEGER DEFAULT 0,
    created_by VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by VARCHAR(50),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TRIGGER update_sets_modtime BEFORE UPDATE ON sets FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE INDEX IF NOT EXISTS idx_set_workout_exercise_id ON sets(workout_exercise_id);

COMMIT TRANSACTION;