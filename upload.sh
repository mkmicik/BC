Echo 'Adding modified files...'
git add *
Echo 'Success!'

Echo 'Commiting changes...'
git commit -m '$(curl -s http://whatthecommit.com/index.txt)'

Echo 'Success!'

Echo 'Pushing to master...!'
git push origin master
Echo 'Success!'