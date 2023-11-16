import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:url_launcher/url_launcher.dart';

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
  List<Webpage> searchResults = [];

  void _performSearch(String query) async {
    List<Webpage>? results = await fetchResults(query);

    setState(() {
      searchResults = results!;
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
              return GestureDetector(
                onTap: () => launchUrl(Uri.parse(searchResults[index].url),
                    webOnlyWindowName: '_blank'),
                child: Card(
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(searchResults[index].url),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child:
                            Text(searchResults[index].text.substring(0, 100)),
                      )
                    ],
                  ),
                ),
              );
            },
          )),
        ],
      ),
    );
  }
}

Future<List<Webpage>?> fetchResults(String query) async {
  final response = await http
      .get(Uri.http('localhost:8080', 'SEARCH-API/', {'search': query}));

  if (response.statusCode == 200) {
    final List<dynamic> results = jsonDecode(response.body) as List<dynamic>;
    return results.map((dynamic result) {
      return Webpage.fromJson(result as Map<String, dynamic>);
    }).toList();
  } else {
    throw Exception('Failed to load results. Response: $response');
  }
}

class Webpage {
  final String url;
  final String text;

  const Webpage({required this.url, required this.text});

  factory Webpage.fromJson(Map<String, dynamic> json) {
    return Webpage(
      url: json['url'] as String,
      text: json['text'] as String,
    );
  }
}
