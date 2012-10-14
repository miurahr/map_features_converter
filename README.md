map features converter
=========================

  Copyright 2012,  Hiroshi Miura <miurahr@osmf.jp>

This is a small utility program to genearete localized XML definition files
from template XML file and localized properties file.

This program intend to help Potlatch2 internationalization, so deeply 
depend on DTD of map_features.xml in Potlatch2.

You can download (or clone) it from
https://github.com/miurahr/map_features_converter/

It will be merged on 
https://github.com/miurahr/potlatch2/

Compile
--------

 This depend on Java and Ant.
 You need to install jdk and ant for compile and test. 

 Run 'ant' on a top of source directory.

 $ ant

Run
--------

 You can use ant to test code.
 run 'ant test' on source directory

 $ ant test

 Test data is located in data/ directory.
 Output data will be located in out directory.

Data files
-------------

Target files

 * data/map_features.xml
 * data/roads.xml
 * data/water.xml
 * data/amenities.xml
 * data/barriers.xml
 * data/buildings.xml
 * data/landuse.xml
 * data/man_made.xml
 * data/paths.xml
 * data/places.xml
 * data/power.xml
 * data/roads.xml
 * data/shopping.xml
 * data/tourism.xml
 * data/transport.xml
 * data/water.xml

Localize definition properties files

 * data/en_US/map_features.properties
 * data/ja_JP/map_features.properties

Process Output

 * out/<locale>/*.xml

 en_US is for template.
 ja_JP is for testing CJK character.

ToDo
-------------

 * Remove comment and ignorable spaces to reduce file size.
