# Example ore list
#
# Copy this file and name it custom_ores.ol if you want to create a custom ore
# list.
# You can list any number of materials to be generated as ore in this list.
#
# Blank rows and rows starting with "#" are ignored.
#
# Fields for each row:
# Name       - Name of the block, this is not used by the generator.
# TypeID     - Block type ID
# Data       - Block data value
# Iterations - Maximum number of veins per chunk
# Amount     - Maximum number of blocks per vein
# MaxHeight  - The maximum height where veins of this type will be created
#
# Do note:
# You must set "generate.extendedore: true" to use type id's above 255 and to
# use the data value. If set to false then the data value will be ignored and
# all ores in the list with a type ID>255 will be ignored
#
# Name, TypeID, Data, Iterations, Amount,MaxHeight
DIRT,          3, 0, 10, 32,  60
GRAVEL,       13, 0, 10, 32,  26
COAL_ORE,     16, 0, 20, 16, 128
IRON_ORE,     15, 0, 20,  8, 128
GOLD_ORE,     14, 0,  2,  8,  26
REDSTONE_ORE, 73, 0,  8,  7,  16
DIAMOND_ORE,  56, 0,  1,  7,  16
LAPIS_ORE,    21, 0,  1,  6,  26
EMERALD_ORE, 129, 0,  1,  6, 128
GRANITE,       1, 1,  8, 32, 128
DIORITE,       1, 3,  8, 32,  64
ANDESITE,      1, 5,  8, 32,  32