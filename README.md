map features converter
=========================

  Copyright 2012  Hiroshi Miura

This is a small utility program to genearete localized XML definition files
from template XML file and localized properties file.

This program intend to help Potlatch2 internationalization so deeply 
depend on DTD of map_features.xml in Potlatch2.

You can download it from
https://github.com/miurahr/map_features_converter

Compile
--------

 run 'ant' on source directory

 $ ant

Run
--------

 run 'ant test' on source directory

Data files
-------------

Target files
 * data/map_features.xml
 * data/roads.xml

Localize definition properties files
 * data/ja_JP/map_features.properties
 * data/en_US/map_features.properties

ToDo
-------------

1. remove comment and spaces to reduce file size
2. support all type of attributes/elements which need to localize








