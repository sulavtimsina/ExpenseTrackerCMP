#!/bin/bash

# Supabase Setup Script for Expense Tracker CMP
echo "🚀 Setting up Supabase for Expense Tracker CMP..."

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is required. Please install Node.js first."
    echo "Visit: https://nodejs.org/"
    exit 1
fi

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is required. Please install npm first."
    exit 1
fi

echo "✅ Node.js and npm found"

# Install Supabase CLI using homebrew (recommended for macOS)
echo "📦 Installing Supabase CLI..."
if command -v brew &> /dev/null; then
    brew install supabase/tap/supabase
elif command -v curl &> /dev/null; then
    # Alternative installation using curl
    curl -L https://github.com/supabase/cli/releases/latest/download/supabase_darwin_amd64.tar.gz | tar -xz
    sudo mv supabase /usr/local/bin/
fi

# Check if Supabase CLI was installed successfully
if ! command -v supabase &> /dev/null; then
    echo "❌ Failed to install Supabase CLI"
    echo "Please install manually from: https://github.com/supabase/cli#install-the-cli"
    exit 1
fi

echo "✅ Supabase CLI installed successfully"

# Create supabase directory if it doesn't exist
mkdir -p supabase

# Initialize Supabase project
echo "🔧 Initializing Supabase project..."
supabase init

# Start local Supabase (for development)
echo "🚀 Starting local Supabase..."
supabase start

# Wait for services to start
echo "⏳ Waiting for services to start..."
sleep 10

# Create database schema
echo "📊 Creating database schema..."

# Create the expenses table
supabase db reset

echo "✅ Supabase setup complete!"
echo ""
echo "📋 Next steps:"
echo "1. Go to https://supabase.com and create a new project"
echo "2. Copy your project URL and anon key"
echo "3. Run the Android/iOS app to test local setup"
echo ""
echo "🔑 Local development credentials:"
echo "Project URL: http://localhost:54321"
echo "Anon Key: $(supabase status | grep 'anon key' | awk '{print $3}')"
echo ""
echo "📖 Visit http://localhost:54323 for local Supabase Studio"
