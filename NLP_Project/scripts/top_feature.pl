#! /usr/bin/perl

use strict;
use warnings;

my @data;
open TEMP, "mymodel";
@data = <TEMP>; 
close TEMP;

my $top = ">op";
open(TOP, $top);

my @array;
my @pos;
my $features = 5;

for(my $i = 0; $i < $features; $i++)
{
		$array[$i] = -1;
		$pos[$i] = -1;
}

for(my $i = 0; $i <= $#data; $i++)
{
		my $no = substr($data[$i], 0, length($data[$i]) - 1);
		my $j = 0;
		if($no > $array[$features - 1])
		{
				my $quit = 0;
				my $j = 0;
				while($quit != 1)
				{
						if($array[$j] < $no)
						{
								my $next_no = $array[$j];
								my $next_pos = $pos[$j];
								
								$array[$j] = $no;
								$pos[$j] = $i + 1;
								
								$j++;

								while($j < $features)
								{
									my $temp = $array[$j];
									$array[$j] = $next_no;
									$next_no = $temp;
									
									$temp = $pos[$j];
									$pos[$j] = $next_pos;
									$next_pos = $temp;
									$j++;
								}

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
