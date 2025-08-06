-- Expense Tracker Database Schema for Supabase
-- This file will be automatically executed when setting up Supabase

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create custom types
CREATE TYPE expense_category AS ENUM (
    'FOOD',
    'TRANSPORTATION', 
    'ENTERTAINMENT',
    'SHOPPING',
    'BILLS',
    'HEALTHCARE',
    'EDUCATION',
    'TRAVEL',
    'OTHER'
);

-- Create expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id TEXT PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    category TEXT NOT NULL,
    note TEXT,
    date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    image_path TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    
    -- Constraints
    CONSTRAINT valid_amount CHECK (amount > 0)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_expenses_user_id ON expenses(user_id);
CREATE INDEX IF NOT EXISTS idx_expenses_date ON expenses(date);
CREATE INDEX IF NOT EXISTS idx_expenses_category ON expenses(category);
CREATE INDEX IF NOT EXISTS idx_expenses_user_date ON expenses(user_id, date DESC);

-- Create updated_at trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_expenses_updated_at 
    BEFORE UPDATE ON expenses 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Row Level Security (RLS) policies
ALTER TABLE expenses ENABLE ROW LEVEL SECURITY;

-- Users can only see their own expenses
CREATE POLICY "Users can view own expenses" ON expenses
    FOR SELECT USING (auth.uid() = user_id);

-- Users can insert their own expenses
CREATE POLICY "Users can insert own expenses" ON expenses
    FOR INSERT WITH CHECK (auth.uid() = user_id);

-- Users can update their own expenses
CREATE POLICY "Users can update own expenses" ON expenses
    FOR UPDATE USING (auth.uid() = user_id);

-- Users can delete their own expenses
CREATE POLICY "Users can delete own expenses" ON expenses
    FOR DELETE USING (auth.uid() = user_id);

-- Create a view for expense analytics
CREATE OR REPLACE VIEW expense_analytics AS
SELECT 
    user_id,
    category,
    DATE_TRUNC('month', date) as month,
    DATE_TRUNC('day', date) as day,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount,
    MIN(amount) as min_amount,
    MAX(amount) as max_amount
FROM expenses
GROUP BY user_id, category, DATE_TRUNC('month', date), DATE_TRUNC('day', date);

-- Grant access to the view
GRANT SELECT ON expense_analytics TO authenticated;

-- Create RLS policy for the view
ALTER VIEW expense_analytics SET (security_invoker = true);

-- Insert sample data (optional - for testing)
-- This will only work if you have a user authenticated
/*
INSERT INTO expenses (user_id, amount, category, note, date) VALUES
(auth.uid(), 25.50, 'FOOD', 'Lunch at restaurant', NOW() - INTERVAL '1 day'),
(auth.uid(), 45.00, 'TRANSPORTATION', 'Gas for car', NOW() - INTERVAL '2 days'),
(auth.uid(), 12.99, 'ENTERTAINMENT', 'Movie tickets', NOW() - INTERVAL '3 days'),
(auth.uid(), 89.99, 'SHOPPING', 'New shirt', NOW() - INTERVAL '4 days'),
(auth.uid(), 120.00, 'BILLS', 'Electric bill', NOW() - INTERVAL '5 days');
*/
