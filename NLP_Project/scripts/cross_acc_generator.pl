#
# SCRIPT To find the average accuracy for all training and test files for a particular n
# To run d script type the command "perl acc_generator n noOfBots"
# n => language Model used
# noOfBots => no of authors
# Training file name format: n.Bot1_Bot2.trn => Trained on Bot1 and Bot2
# Test file name format: n.Bot1_Bot2.tst => Tested on Bot1 but feature vector from Bot1 and Bot2
#

#! /usr/bin/perl

use strict;
use warnings;

my $n = $ARGV[0];
my $accuracy = ">".$n.".accuracy";
my $noOfBots = $ARGV[1];
my $trainPath = "../classifier/./train";
my $predictPath = "../classifier/./predict";
my $trainDataPath = "../train/blog/bow/";
my $testDataPath = "../test/blog/bow/"; 

open(ACCURACY, $accuracy);

for(my $i = 1; $i <= $noOfBots; $i++ )
{
		for(my $j = $i + 1; $j <= $noOfBots; $j++)
		{
				for(my $k = 1; $k <=5; $k++)
				{
						system($trainPath . " " . $trainDataPath . $n . "." . $k . "." . $i . "_" . $j . ".trn mymodel");
						system($predictPath . " " . $testDataPath . $n . "." . $k . "." . $i . "_" . $j . ".tst mymodel output >> temp");
						system($predictPath . " " . $testDataPath . $n . "." . $k . "." . $j . "_" . $i . ".tst mymodel output >> temp");					
				}
		}
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
