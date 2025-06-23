#!/bin/bash

# Simplified Supabase Setup Script for Expense Tracker CMP
echo "🚀 Setting up Supabase for Expense Tracker CMP..."

# Check if Supabase CLI is installed
if ! command -v supabase &> /dev/null; then
    echo "❌ Supabase CLI not found. Please install it first:"
    echo ""
    echo "macOS (Homebrew):"
    echo "  brew install supabase/tap/supabase"
    echo ""
    echo "Other platforms:"
    echo "  Visit: https://github.com/supabase/cli#install-the-cli"
    echo ""
    exit 1
fi

echo "✅ Supabase CLI found"

# Create supabase directory if it doesn't exist
mkdir -p supabase

# Initialize Supabase project
echo "🔧 Initializing Supabase project..."
supabase init

# Check if migrations directory exists, create if not
mkdir -p supabase/migrations

# Apply the database schema
echo "📊 Applying database schema..."
if [ -f "supabase/migrations/20240101000000_initial_schema.sql" ]; then
    echo "✅ Database schema file found"
else
    echo "❌ Database schema file not found. Please ensure the migration file exists."
    exit 1
fi

# Start local Supabase (for development)
echo "🚀 Starting local Supabase..."
supabase start

# Wait for services to start
echo "⏳ Waiting for services to start..."
sleep 10

# Apply migrations
echo "📊 Applying migrations..."
supabase db reset

echo ""
echo "✅ Supabase setup complete!"
echo ""
echo "📋 Next steps:"
echo "1. Go to https://supabase.com and create a new project"
echo "2. Copy your project URL and anon key"
echo "3. Update SupabaseClient.kt with your production keys"
echo "4. Build and run your app to test the integration"
echo ""
echo "🔑 Local development info:"
echo "Project URL: http://localhost:54321"
echo "Supabase Studio: http://localhost:54323"
echo ""

# Try to get the anon key
if supabase status > /dev/null 2>&1; then
    echo "Anon Key: $(supabase status --output json | jq -r '.anon_key' 2>/dev/null || echo 'Run: supabase status to get the anon key')"
fi

echo ""
echo "🚀 Ready to build! Run: ./gradlew :composeApp:run"
