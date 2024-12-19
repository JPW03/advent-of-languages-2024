#include <fstream>
#include <iostream>
#include <ostream>
#include <string>
#include <utility>
#include <vector>
#include <limits>

using namespace std;

// Compilation command: g++ -std=c++17 -Wall -Wextra -O2 day-1.cpp -o day-1

int popSmallest(vector<int> &vector) {
    int lowest = numeric_limits<int>::max();
    int iOfLowest = -1;
    for (int i = 0; i < vector.size(); i++) {
        int n = vector.at(i);
        if (n < lowest) {
            lowest = n;
            iOfLowest = i;
        }
    }
    vector.erase(vector.begin() + iOfLowest);
    return lowest;
}

vector<pair<int, int> > getLocationIds(vector<int> leftLocationIds, vector<int> rightLocationIds) {
    vector<pair<int, int> > locationIds;

    int totalNumberOfIds = leftLocationIds.size();

    for (int i = 0; i < totalNumberOfIds; i++) {
        int leftLowest = popSmallest(leftLocationIds);
        int rightLowest = popSmallest(rightLocationIds);
        locationIds.push_back(*new pair<int, int>(leftLowest, rightLowest));
    }

    return locationIds;
}

pair<vector<int>, vector<int> > getExampleInput() {
    
    vector<int> leftLocationIds;
    leftLocationIds.push_back(3);
    leftLocationIds.push_back(4);
    leftLocationIds.push_back(2);
    leftLocationIds.push_back(1);
    leftLocationIds.push_back(3);
    leftLocationIds.push_back(3);
    
    vector<int> rightLocationIds; 
    rightLocationIds.push_back(4);
    rightLocationIds.push_back(3);
    rightLocationIds.push_back(5);
    rightLocationIds.push_back(3);
    rightLocationIds.push_back(9);
    rightLocationIds.push_back(3);

    return *new pair<vector<int>, vector<int> >(leftLocationIds, rightLocationIds);
}


int main() {
   
    // Actual input
    vector<int> leftLocationIds;
    vector<int> rightLocationIds;

    ifstream inputFile("day-1-input");
    if (inputFile.is_open()) {
        string line;
        while (getline(inputFile, line)) {
            cout << line << endl;
            string left = line.substr(0,5);
            string right = line.substr(8,5);
            leftLocationIds.push_back(stoi(left));
            rightLocationIds.push_back(stoi(right));
        }
        inputFile.close();
    }

    vector<pair<int, int> > locationIds = getLocationIds(leftLocationIds, rightLocationIds);

    // Example input
    //pair<vector<int>, vector<int> > exampleInput = getExampleInput();
    //vector<pair<int, int> > locationIds = getLocationIds(exampleInput.first, exampleInput.second);

    cout << "Pairs:" << endl;
    for (int i = 0; i < locationIds.size(); i++) {
        pair<int, int> locationId = locationIds.at(i);
        cout << locationId.first << "," << locationId.second << endl;
    }

    cout << "Distances:" << endl;
    vector<int> distances;
    int totalDistance = 0;
    for (const pair<int, int> &locationId : locationIds) {
        int dist = abs(locationId.first - locationId.second);
        cout << dist << endl;
        distances.push_back(dist);
        totalDistance += dist;
    }

    cout << "Total Distance: " << totalDistance << endl;

    return 0;
}
