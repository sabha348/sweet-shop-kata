-- Add the category column
ALTER TABLE sweets ADD COLUMN category VARCHAR(255);

-- Set a default value for existing sweets so they aren't null
UPDATE sweets SET category = 'General' WHERE category IS NULL;

-- Make it not null after populating data (optional, but good practice)
ALTER TABLE sweets ALTER COLUMN category SET NOT NULL;