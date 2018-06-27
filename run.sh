#!/bin/bash
JRE_EXEC=/usr/lib/jvm/java-8-oracle/jre/bin/java

$JRE_EXEC -version
echo
echo  ' ______   _                       _              _                       _         '      
echo  '(____  \ (_)                     (_)            | |                     | |        '      
echo  ' ____)  ) _  ____    ____   ____  _   ____       \ \    ____  ____    _ | |  ____   ____' 
echo  '|  __  ( | ||  _ \  / _  | / ___)| | / _  |       \ \  / _  )|  _ \  / || | / _  ) / ___)'
echo  '| |__)  )| || | | |( ( | || |    | |( ( | |   _____) )( (/ / | | | |( (_| |( (/ / | |    '
echo  '|______/ |_||_| |_| \_||_||_|    |_| \_||_|  (______/  \____)|_| |_| \____| \____)|_|    '
echo
echo Running Binaria Sender V1  
echo
echo Las salidas se almacenan en el archivo binaria.sender.log 
echo
echo "PID of this script: $$"
echo
echo
# Script to run a process that runs in the background, 
# then tail the log file in the forground. 
# Useful for running apps in Docker container  like subsonic, spark or apache.
# Author: dan.isla@gmail.com
# Funcion que detiene los procesos cargados en memoria
function cleanup {
#  kill `pidof tail` 2>/dev/null
  kill `pidof java` 2>/dev/null
}
# Captura las señales de kill enviadas por el OS, para este caso es -1 (EXIT) -2 (INT)
trap cleanup EXIT
trap cleanup INT

cleanup # Elimina los procesos por si quedó uno sin detener

# Set file to tail
#WATCH_FILE="/var/log/messages"

### Execute java daemonized program
$JRE_EXEC -jar binaria.sender.jar $$
# Evalua si se generó un error y lo imprime, no afecta la ejecución
RES=$?
if [[ ! ${RES} -eq 0 ]]; then
  echo "ERROR: Exit code was ${RES}"
  exit 1
fi

#while ! stat ${WATCH_FILE} >/dev/null 2>&1; do
#  echo "Waiting for ${WATCH_FILE}"
#  sleep 1
#done

#tail -f ${WATCH_FILE} &

# envia la señal par detener el proceso (evaluar como funciona con hilos ya que
# la señal -9 no puede ser capturada usando trap por motivos de seguridad)
while kill -0 `pidof java` 2>/dev/null; do
  sleep 0.5
done
