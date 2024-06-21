ffmpeg -framerate 1/3 -pattern_type glob -i '*.png' -vf "fps=10,scale=320:-1" output.gif
