
espeak -ves-la -g20 -s150 Hola$1".... Soy neobot, el nuevo robot de inteligencia artificial de osde. Te voy a enseñar hábitos saludables como solo un robot puede hacerlo. De manera precisa y programada." -w saludo_$1.wav
opusenc saludo_$1.wav presentacion_$1.opus
rm saludo_$1.wav
