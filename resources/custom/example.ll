# This is an example custom Loot List.
#
# Each row specifies a possible loot.
#
# When generating a loot chest the plugin will select an item randomly
# from the list below, if the probability value matches a dice throw then
# it's added to the chest. A new item is randomly selected again as before,
# then a new one, and a new one until the chest contains the correct number
# of stacks.
#
# Columns:
#   Name:  A name, just used for your readability. The plugin ignores this.
#   MaterialID: The ID of an item
#   Data: The Material data
#   Min: Minimum stack count
#   Max: Maximum stack count
#   Probability: 1-100 The probability of this item being added to the loot
#                if selected by the plugin.
#
#  Example:
#   Leather, 334, 0, 1, 4, 5
#
#   Name = CowHide, ignored
#   MaterialID = 334 (Leather)
#   Data = 0
#   Min = 1. If selected then the stack will be at least 1 item.
#   Max = 4. If selected then the stack will be 4 items at most.
#   Probability = 5. When the plugin selects this item there's a 5%
#                    chance that it will actually be added.
#
# Name,MaterialID,Data,Min,Max,Probability
Leather     ,334,0,1,4,5
RawPorkchop ,319,0,1,4,5
LeatherCap  ,298,0,1,1,20
LeatherTunic,299,0,1,1,20
LeatherPants,300,0,1,1,20
LeatherBoots,301,0,1,1,20