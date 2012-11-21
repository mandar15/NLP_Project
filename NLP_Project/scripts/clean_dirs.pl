#
# SCRIPT to Clean test and training files from test and train folders.
#

system("rm -rf ../train/blog/pos/*");
system("rm -rf ../train/blog/bow/*");
system("rm -rf ../train/blog/ngram/*");
system("rm -rf ../train/tweet/pos/*");
system("rm -rf ../train/tweet/bow/*");
system("rm -rf ../train/tweet/ngram/*");

system("rm -rf ../test/blog/pos/*");
system("rm -rf ../test/blog/bow/*");
system("rm -rf ../test/blog/ngram/*");
system("rm -rf ../test/tweet/pos/*");
system("rm -rf ../test/tweet/bow/*");
system("rm -rf ../test/tweet/ngram/*");

