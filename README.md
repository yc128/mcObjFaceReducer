# Minecraft Schematic to OBJ Model Optimizer

## Overview
This tool optimizes 3D models generated from Minecraft schematics by significantly reducing the number of faces in the output `.obj` models. It merges adjacent rectangular faces within the same group that share the same orientation, creating a simpler and more efficient model.

## Features
- **Face Reduction**: Merges adjacent, identically oriented rectangular faces into larger rectangles, reducing the overall face count.
- **Performance Improvement**: Models with fewer faces improve rendering times and lower memory usage in 3D applications.
- **Easier Editing**: Simplified models make manual adjustments in 3D software less cumbersome.
- **Better Visuals**: Reduces visual artifacts for cleaner and more appealing 3D models.

## Usage
The tool automatically analyzes and optimizes `.obj` models generated from Minecraft schematics. Ensure your model file is in the correct format, change the path in main.java to your target .obj file and run the optimizer to produce an enhanced model with fewer faces.
