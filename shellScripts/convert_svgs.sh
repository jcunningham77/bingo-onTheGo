#!/bin/bash

# Navigate to project root directory
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

# Source directory with SVGs
SVG_DIR="composeApp/src/commonMain/composeResources/drawable"

# Platform-specific output directories
ANDROID_DIR="composeApp/src/androidMain/composeResources/drawable"
IOS_DIR="composeApp/src/iosMain/composeResources/drawable"

# Direct path to svg2vector.jar (update this path to your actual location)
SVG2VECTOR="$HOME/svg2vector.jar"

if [ ! -f "$SVG2VECTOR" ]; then
  echo "Error: svg2vector.jar not found at $SVG2VECTOR"
  echo "Please update the SVG2VECTOR path in the script"
  exit 1
fi

# Create platform-specific directories
mkdir -p "$ANDROID_DIR"
mkdir -p "$IOS_DIR"

echo "Converting SVGs for cross-platform support..."
echo "Working directory: $(pwd)"
echo "SVG directory: $SVG_DIR"
echo "Checking for SVG files..."

# Debug: Check if SVG directory exists
if [ ! -d "$SVG_DIR" ]; then
  echo "Error: SVG directory not found: $SVG_DIR"
  exit 1
fi

# Debug: List all files in SVG directory
echo "Files in SVG directory:"
ls -la "$SVG_DIR"

for svg in "$SVG_DIR"/*.svg; do
  echo "current svg = $svg"
  # Check if the glob pattern returned actual files (not the literal pattern)
  if [ "$svg" = "$SVG_DIR/*.svg" ]; then
    echo "No SVG files found in $SVG_DIR"
    break
  fi
  if [ -f "$svg" ]; then
    filename=$(basename "$svg" .svg)
    
    # Try to convert to XML Vector Drawable for Android
    xml_output="$ANDROID_DIR/${filename}.xml"
    java -jar "$SVG2VECTOR" -c "$svg" -o "$xml_output" >/dev/null 2>&1

    # Check if the XML file was actually created
    if [ -f "$xml_output" ]; then
      echo "✓ Processed: $filename (Android: XML, iOS: SVG)"
    else
      echo "⚠ Warning: Could not convert $filename to XML. Copying SVG to Android as fallback."
      cp "$svg" "$ANDROID_DIR/"
    fi

    # Copy SVG to iOS directory
    cp "$svg" "$IOS_DIR/"
  fi
done

echo ""
echo "Conversion complete!"
echo "- Original SVGs remain in: $SVG_DIR"
if ls "$ANDROID_DIR"/*.xml >/dev/null 2>&1; then
  echo "- Android XML vectors: $ANDROID_DIR"
else
  echo "- Android SVGs (fallback): $ANDROID_DIR"
  echo "  Note: XML conversion failed. You may need to manually convert SVGs to Vector Drawables in Android Studio."
fi
echo "- iOS SVGs: $IOS_DIR"
echo ""
echo "Now rebuild your project to generate Res.drawable references"
