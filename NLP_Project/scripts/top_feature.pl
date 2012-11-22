#! /usr/bin/perl

use strict;
use warnings;

my @data;
open TEMP, "MYMODEL";
@data = <TEMP>; 
close TEMP;

my $top = ">op";
open(TOP, $top);

my @array;
my @pos;
my $features = 20;

for(my $i = 0; $i < $features; $i++)
{
	$array[$i] = -1;
	$pos[$i] = -1;
}

for(my $i = 0; $i <= $#data; $i++)
{
	my $no = substr($data[$i], 0, length($data[$i]) - 1);
	if($no > $array[$features - 1])
	{
		my $quit = 0;
		my $j = 0;
		while($quit != 1)
		{
			if($array[$j] < $no)
			{
				$array[$j] = $no;
				$pos[$j] = $i + 1;
				$quit = 1;
			}
			$j++;
		}		
	}
}

for(my $i = 0; $i < $features; $i++)
{
	print TOP $pos[$i]." ".$array[$i]."\n";
}

close TOP;
