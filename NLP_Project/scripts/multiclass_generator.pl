#
# SCRIPT To find the average accuracy for all training and test files for a particular n
# To run d script type the command "perl acc_generator n noOfBots"
# n => language Model used
# noOfBots => no of authors
# Training file name format: n.<crossfold number>.trn => Trained on Bot1 and Bot2
# Test file name format: n.<crossfold_number>.tst => Tested on Bot1 but feature vector from Bot1 and Bot2
#

#! /usr/bin/perl

use strict;
use warnings;

my $n = $ARGV[0];
my $accuracy = ">".$n.".accuracy";
my $noOfBots = $ARGV[1];
my $trainPath = "../classifier/./train";
my $predictPath = "../classifier/./predict";
my $trainDataPath = "/media/DEF8DBF5F8DBC9C3/NLP/multiclass/train/blog/bow/trigram/";
my $testDataPath = "/media/DEF8DBF5F8DBC9C3/NLP/multiclass/test/blog/bow/trigram/";

open(ACCURACY, $accuracy);

my $noOfCrossFolds = 5;

for(my $i = 0; $i < $noOfCrossFolds; $i++ )
{
	system($trainPath . " " . $trainDataPath . $n . "." . $i . ".trn mymodel");
	system($predictPath . " " . $testDataPath . $n . "." . $i . ".tst mymodel output >> temp");
}

my @accuracies;
open TEMP, "temp";
@accuracies = <TEMP>; 
close TEMP;

my $sum = 0;
for(my $i = 0; $i <= $#accuracies; $i++)
{
		my @line = split(' ', $accuracies[$i]);
		my $accuracy = substr($line[2], 0, length($line[2]) - 1);
		$sum += $accuracy;
}

my $output = $sum/($#accuracies + 1);
$output = $n." ".$output."\n";

print ACCURACY $output;
close ACCURACY;

system("rm temp");
