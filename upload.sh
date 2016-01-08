Echo 'Adding modified files...'
git add *
Echo 'Success!'

Echo 'Commiting changes...'
message=($curl http://whatthecommit.com/index.txt)
git commit -m "$message"
Echo 'Success!'

Echo 'Pushing to master...!'
git push origin master
Echo 'Success!'