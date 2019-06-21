package de.bkroeger.editor4.model;

public enum SectionModelType {
	File,	// eine Datei
	Connector,	// die Konnektoren
	Style,	// die Style-Eigenschaften
	Page,	// eine Seite
	Shape,	// ein 1D oder 2D Shape
	Path,	// ein Pfad
	PathElement,	// Pfadelement
	Master,	// ein Shape-Master
	Location,	// Position des Shapes; Parent-Koordinaten
	CenterPoint	// Position des CenterPoint innerhalb des Shapes; lokale Koordinaten
	}
