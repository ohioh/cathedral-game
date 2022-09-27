# Importieren der Pygame-Bibliothek
from io import TextIOWrapper
from socket import INADDR_NONE
import pygame, sys, time, random
from pygame.locals import *

# unser Multiplikator 
MULTIPLIKATOR = 20

# Spielfeld erzeugen über Berechnung
fenster = pygame.display.set_mode((20 * MULTIPLIKATOR, 30 * MULTIPLIKATOR))

# Titel für Fensterkopf
pygame.display.set_caption("Breakout in Python")
spielaktiv = True

# Bildschirm Aktualisierungen einstellen
clock = pygame.time.Clock()

# genutzte Farben
GRAU  =     (192,192,192)
SCHWARZ =   (  0,   0,   0)
WEISS   =   ( 255, 255, 255)

fieldSizeX = 10
fieldSizeY = 10

#Figures
TAVERN = 1
STABLE = 2
INN = 3
BRIDGE = 3
SQUARE = 4
MANER = 4
ABBEY = 4
ACADEMY = 5
INFIRMARY = 5
CASTLE = 5
TOWER = 5
CATHEDRAL = 5

TAVERN_FORM = [[1]]
STABLE_FORM = [[1,1],[[1],[1]]]
INN_FORM = [[[1,1],[0,1]],[[1,1],[1,1]],]
BRIDGE_FORM = [[1]]
SQUARE_FORM = [[1]]
MANER_FORM = [[1]]
ABBEY_FORM = [[1]]
ACADEMY_FORM = [[1]]
INFIRMARY_FORM = [[1]]
CASTLE_FORM = [[1]]
TOWER_FORM = [[1]]
CATHEDRAL_FORM = [[1]]




# Spielfeld mit Mauersteinen 
karte=[
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0],
[0,0,0,0,0,0,0,0,0,0]
] 

# Hintergrundfarbe Fenster
fenster.fill(WEISS)

# Korrekturfaktor berechnen
def kor(zahl):
    zahl = zahl * MULTIPLIKATOR
    return zahl

# Spielelement zeichnen
def element_zeichnen(spalte,reihe):
    pygame.draw.rect(fenster, GRAU, [kor(spalte)+1, kor(reihe)+1,kor(1)-1,kor(1)-1])

# Ausgabe Mauersteine im Spielfenster
for x in range(0,fieldSizeX):
    for y in range(0,fieldSizeY):
        if karte[y][x] != 0:
            element_zeichnen(x,y)

# Schleife Hauptprogramm
while spielaktiv:
    # Überprüfen, ob Nutzer eine Aktion durchgeführt hat
    for event in pygame.event.get():
        if event.type == pygame.QUIT or event.type == pygame.KEYDOWN and event.key == pygame.K_ESCAPE:
            spielaktiv = False
            print("Spieler hat beendet")

    # Fenster aktualisieren
    pygame.display.flip()

    # Refresh-Zeiten festlegen
    clock.tick(10)

pygame.quit()