import 'package:flutter/material.dart';

void main() {
  runApp(const SearchEngineApp());
}

class SearchEngineApp extends StatelessWidget {
  const SearchEngineApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: const SearchPage(),
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: Colors.green,
        brightness: Brightness.dark,
      ),
    );
  }
}

class SearchPage extends StatefulWidget {
  const SearchPage({super.key});

  @override
  State<SearchPage> createState() => _SearchPageState();
}

class _SearchPageState extends State<SearchPage> {
  final TextEditingController _searchController = TextEditingController();
  List<String> searchResults = [];

  void _performSearch(String query) {
    // TODO: Implement search
    List<String> results = [
      "Result 1 for $query",
      "Result 2 for $query",
      "Result 3 for $query",
    ];

    setState(() {
      searchResults = results;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Search Engine"),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 16, bottom: 16),
            child: FractionallySizedBox(
              widthFactor: 0.5,
              child: TextField(
                controller: _searchController,
                decoration: InputDecoration(
                  border: const OutlineInputBorder(),
                  labelText: "Search",
                  suffixIcon: IconButton(
                    icon: const Icon(Icons.search),
                    onPressed: () {
                      _performSearch(_searchController.text);
                    },
                  ),
                ),
                onSubmitted: (value) => _performSearch(value),
              ),
            ),
          ),
          Expanded(
              child: ListView.builder(
            itemCount: searchResults.length,
            itemBuilder: (context, index) {
              return Column(
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [Text(searchResults[index])],
                  )
                ],
              );
            },
          )),
        ],
      ),
    );
  }
}
