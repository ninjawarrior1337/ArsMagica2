BRANCH="1.10.2"

if ["$TRAVIS_BRANCH" = "$BRANCH"]; then

	if [ "$TRAVIS_PULL_REQUEST" = false ]; then

		if [ -z "$TRAVIS_TAG" ]; then

			echo -e "Creating tag...\n"
			git config --global user.email "Growlith1223@gmail.com"
			git config --global user.name "Growlith"
			git tag -a v${TRAvIS_BUILD_NUMBER} -m "Automated build with Travis-CI"
			git push origin --tags
			git fetch origin

			echo -e "Tag created!\n"
		fi
	fi
fi