## Transitioning [The Live Site](http://www.whateverorigin.org/).

*TLDR: Code will be rewritten in Go and the live site will be under new management while seeing **no functionality change.***

Currently the Java backend has become sluggish and outdated by modern standards and we've decided to rewrite it in Go, the Java code will remain accessible on the `java` branch.

This transition will not effect your usage. We will start by allocating small amounts of the traffic to the new server (to lower the risk of outages).

| Date | Allocation | State |
| -- | -- | -- |
| January 10th | 1% | ✅ |
| January 17th | 10% | ✅ |
| January 24th | 25% | ✅ |
| January 31st | 50% | ✅ |
| Febuary 7th |  75% | ✅ |
| Febuary 14th | 100% | ⏺ |

-----


Whatever Origin is an open source alternative to AnyOrigin.com

When I was facing Same Origin problems while developing [Bitcoin Pie](http://bitcoinpie.com/), I was excited to discover how anyorigin.com solved the issue for me ... only, a week later it stopped working for some https sites.

For example, right now try and feed https://bitcointalk.org/ into anyorigin and you'll get an ugly "null" as the output.

Having recently discovered Heroku and Play!, I found that deploying a simple server app is no longer a big deal, and so made out to develop a simple, open source alternative to Any Origin.

[Live site](http://whateverorigin.org/).
